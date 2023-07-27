package ru.otus.kotlin.brown.repo.postgresql

import javax.sql.DataSource
import org.jetbrains.exposed.sql.*
import java.util.NoSuchElementException
import ru.otus.kotlin.brown.common.repo.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.kotlin.brown.common.models.NotificationId
import ru.otus.kotlin.brown.common.helpers.asNotificationError
import ru.otus.kotlin.brown.common.models.NotificationUserId
import ru.otus.kotlin.brown.repo.postgresql.entity.NotificationTable
import ru.otus.kotlin.brown.repo.postgresql.mappers.toInsertStatement
import ru.otus.kotlin.brown.repo.postgresql.mappers.toNotification
import ru.otus.kotlin.brown.repo.postgresql.mappers.toUpdateStatement

class PostgresqlRepo (pool: DataSource) : INotificationRepository {

    init {
        Database.connect(pool)
    }

    private inline fun <reified T : Any> transactionWrapper(crossinline block: () -> T): T =
        try {
            transaction {
                block()
            }
        } catch (e: Exception) {
            when {
                DbNotificationResponse is T -> DbNotificationResponse.error(e.asNotificationError()) as T
                DbNotificationsResponse is T -> DbNotificationsResponse.error(e.asNotificationError()) as T
                else -> throw Exception(e.message, e)
            }
        }

    private fun read(id: NotificationId): DbNotificationResponse {
        val notification = NotificationTable.select { NotificationTable.id eq id.asInt() }
            .singleOrNull()
            ?.toNotification() ?: return DbNotificationResponse.errorNotFound

        return DbNotificationResponse.success(notification)
    }

    override suspend fun createNotification(rq: DbNotificationRequest): DbNotificationResponse = transactionWrapper {
        val createdNotification = NotificationTable
            .insert { rq.notification.toInsertStatement(it) }
            .resultedValues
            ?.singleOrNull()
            ?.toNotification() ?: throw NoSuchElementException("Error creating notification")

        DbNotificationResponse.success(createdNotification)
    }

    override suspend fun readNotification(rq: DbNotificationIdRequest): DbNotificationResponse = transactionWrapper {
        read(rq.id)
    }

    override suspend fun updateNotification(rq: DbNotificationRequest): DbNotificationResponse = transactionWrapper {
        if (rq.notification.id == NotificationId.NONE) return@transactionWrapper DbNotificationResponse.errorEmptyId

        val currentNotification = NotificationTable.select { NotificationTable.id eq rq.notification.id.asInt() }
            .singleOrNull()
            ?.toNotification()

        when {
            currentNotification == null -> DbNotificationResponse.errorNotFound
            currentNotification.lock != rq.notification.lock -> DbNotificationResponse.errorConcurrent(rq.notification.lock, currentNotification)
            else -> {
                NotificationTable.update({ NotificationTable.id eq rq.notification.id.asInt() }) { rq.notification.toUpdateStatement(it) }
                read(rq.notification.id)
            }
        }
    }

    override suspend fun cancelNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
        TODO("Change method for closeNotification at the next refactoring")
    }

    override suspend fun searchNotification(rq: DbNotificationFilterRequest): DbNotificationsResponse =
        transactionWrapper {
            val result = NotificationTable.select {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != NotificationUserId.NONE) {
                        add(NotificationTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.notificationType != null) {
                        add(NotificationTable.type eq rq.notificationType!!)
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (NotificationTable.title like "%${rq.titleFilter}%")
                                    or (NotificationTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbNotificationsResponse(data = result.map { it.toNotification() }, isSuccess = true)
        }
}
