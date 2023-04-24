package ru.otus.kotlin.brown.common

import kotlinx.datetime.Instant
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.stubs.NotificationStubs

data class NotificationContext(
    var command: NotificationCommand = NotificationCommand.NONE,
    var state: NotificationState = NotificationState.NONE,
    val errors: MutableList<NotificationError> = mutableListOf(),

    var workMode: NotificationWorkMode = NotificationWorkMode.PROD,
    var stubCase: NotificationStubs = NotificationStubs.NONE,

    var requestId: NotificationRequestId = NotificationRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var notificationRequest: Notification = Notification(),
    var notificationFilterRequest: NotificationFilter = NotificationFilter(),
    var notificationResponse: Notification = Notification(),
    var notificationsResponse: MutableList<Notification> = mutableListOf(),
)
