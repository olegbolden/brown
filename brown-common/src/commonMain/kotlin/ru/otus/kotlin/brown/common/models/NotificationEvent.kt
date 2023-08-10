package ru.otus.kotlin.brown.common.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class NotificationEvent(
    var id: NotificationId = NotificationId.NONE,
    var index: String = "",
    var status: NotificationEventStatus = NotificationEventStatus.OPEN,
    var type: NotificationType = NotificationType.COMMON,
    var createdAt: Instant = Clock.System.now()
) {
    companion object {
        val NONE = NotificationEvent()
    }
}