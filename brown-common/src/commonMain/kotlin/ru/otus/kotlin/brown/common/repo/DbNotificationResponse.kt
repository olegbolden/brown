package ru.otus.kotlin.brown.common.repo

import ru.otus.kotlin.brown.common.models.*

data class DbNotificationResponse(
    override val data: Notification?,
    override val isSuccess: Boolean,
    override val errors: List<NotificationError> = emptyList()
): IDbResponse<Notification> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbNotificationResponse(null, true)

        fun success(result: Notification) = DbNotificationResponse(result, true)
        fun error(error: NotificationError) = DbNotificationResponse(null, false, listOf(error))
        fun error(errors: List<NotificationError>) = DbNotificationResponse(null, false, errors)
    }
}
