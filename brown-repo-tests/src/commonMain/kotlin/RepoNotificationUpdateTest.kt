package ru.otus.kotlin.brown.repo.tests

import kotlin.test.*
import ru.otus.kotlin.brown.common.repo.*
import ru.otus.kotlin.brown.common.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNotificationUpdateTest {
    abstract val repo: INotificationRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = NotificationId("notification-repo-update-not-found")
    protected val lockBad = NotificationLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = NotificationLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Notification(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = NotificationUserId("owner-123"),
            visibility = NotificationVisibility.PUBLIC,
            notificationType = NotificationType.ALERT,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Notification(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = NotificationUserId("owner-123"),
        visibility = NotificationVisibility.PUBLIC,
        notificationType = NotificationType.ALERT,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        Notification(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = NotificationUserId("owner-123"),
            visibility = NotificationVisibility.PUBLIC,
            notificationType = NotificationType.ALERT,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateNotification(DbNotificationRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(reqUpdateSucc.notificationType, result.data?.notificationType)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateNotification(DbNotificationRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateNotification(DbNotificationRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitNotifications("update") {
        override val initObjects: List<Notification> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
