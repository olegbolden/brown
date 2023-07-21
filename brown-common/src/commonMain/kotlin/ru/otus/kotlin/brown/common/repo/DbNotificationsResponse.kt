package ru.otus.kotlin.brown.common.repo

import ru.otus.kotlin.brown.common.models.*

data class DbNotificationsResponse(
    override val data: List<Notification>?,
    override val isSuccess: Boolean,
    override val errors: List<NotificationError> = emptyList(),
): IDbResponse<List<Notification>> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbNotificationsResponse(emptyList(), true)

        fun success(result: List<Notification>) = DbNotificationsResponse(result, true)
        fun error(error: NotificationError) = DbNotificationsResponse(null, false, listOf(error))
        fun error(errors: List<NotificationError>) = DbNotificationsResponse(null, false, errors)
    }
}
