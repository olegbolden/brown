package ru.otus.kotlin.brown.common.repo

import ru.otus.kotlin.brown.common.helpers.errorRepoConcurrency
import ru.otus.kotlin.brown.common.models.Notification
import ru.otus.kotlin.brown.common.models.NotificationError
import ru.otus.kotlin.brown.common.models.NotificationLock
import ru.otus.kotlin.brown.common.helpers.errorEmptyId as notificationErrorEmptyId
import ru.otus.kotlin.brown.common.helpers.errorNotFound as notificationErrorNotFound

data class DbNotificationResponse(
    override val data: Notification?,
    override val isSuccess: Boolean,
    override val errors: List<NotificationError> = emptyList()
): IDbResponse<Notification> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbNotificationResponse(null, true)

        fun success(result: Notification) = DbNotificationResponse(result, true)
        fun error(error: NotificationError, data: Notification? = null) = DbNotificationResponse(data, false, listOf(error))
        fun error(errors: List<NotificationError>, data: Notification? = null) = DbNotificationResponse(data, false, errors)

        fun errorConcurrent(lock: NotificationLock, notification: Notification?) = error(
            errorRepoConcurrency(lock, notification?.lock?.let { NotificationLock(it.asString()) }),
            notification
        )

        val errorEmptyId = error(notificationErrorEmptyId)
        val errorNotFound = error(notificationErrorNotFound)
    }
}
