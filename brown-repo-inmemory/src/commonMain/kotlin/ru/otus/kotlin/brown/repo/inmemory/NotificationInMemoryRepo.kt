package ru.otus.kotlin.brown.repo.inmemory

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import com.benasher44.uuid.uuid4
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.github.reactivecircus.cache4k.Cache
import ru.otus.kotlin.brown.common.helpers.errorRepoConcurrency
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.repo.*
import ru.otus.kotlin.brown.repo.inmemory.entity.NotificationEntity

class NotificationInMemoryRepo(
    initObjects: List<Notification> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : INotificationRepository {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, NotificationEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(notification: Notification) {
        val entity = NotificationEntity(notification)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createNotification(rq: DbNotificationRequest): DbNotificationResponse {
        val key = randomUuid()
        val notification = rq.notification.copy(id = NotificationId(key), lock = NotificationLock(randomUuid()))
        val entity = NotificationEntity(notification)
        cache.put(key, entity)
        return DbNotificationResponse(
            data = notification,
            isSuccess = true,
        )
    }

    override suspend fun readNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
        val key = rq.id.takeIf { it != NotificationId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbNotificationResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateNotification(rq: DbNotificationRequest): DbNotificationResponse {
        val key = rq.notification.id.takeIf { it != NotificationId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.notification.lock.takeIf { it != NotificationLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newNotification = rq.notification.copy(lock = NotificationLock(randomUuid()))
        val entity = NotificationEntity(newNotification)
        return mutex.withLock {
            val oldNotification = cache.get(key)
            when {
                oldNotification == null -> resultErrorNotFound
                oldNotification.lock != oldLock -> DbNotificationResponse(
                    data = oldNotification.toInternal(),
                    isSuccess = false,
                    errors = listOf(errorRepoConcurrency(NotificationLock(oldLock), oldNotification.lock?.let { NotificationLock(it) }))
                )

                else -> {
                    cache.put(key, entity)
                    DbNotificationResponse(
                        data = newNotification,
                        isSuccess = true,
                    )
                }
            }
        }
    }

    override suspend fun cancelNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
        val key = rq.id.takeIf { it != NotificationId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != NotificationLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldNotification = cache.get(key)
            when {
                oldNotification == null -> resultErrorNotFound
                oldNotification.lock != oldLock -> DbNotificationResponse(
                    data = oldNotification.toInternal(),
                    isSuccess = false,
                    errors = listOf(errorRepoConcurrency(NotificationLock(oldLock), oldNotification.lock?.let { NotificationLock(it) }))
                )

                else -> {
                    cache.invalidate(key)
                    DbNotificationResponse(
                        data = oldNotification.toInternal(),
                        isSuccess = true,
                    )
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchNotification(rq: DbNotificationFilterRequest): DbNotificationsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != NotificationUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.notificationType.takeIf { it != null }?.let {
                    rq.notificationType?.name == entry.value.notificationType
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbNotificationsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbNotificationResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                NotificationError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbNotificationResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                NotificationError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbNotificationResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                NotificationError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}
