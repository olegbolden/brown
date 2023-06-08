package ru.otus.kotlin.brown.common

import kotlinx.datetime.Instant
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.stubs.NotificationStubType
import ru.otus.kotlin.brown.common.models.NotificationFilter

data class NotificationContext(
    var command: NotificationCommand = NotificationCommand.NONE,
    var state: NotificationState = NotificationState.NONE,
    val errors: MutableList<NotificationError> = mutableListOf(),

    var workMode: NotificationWorkMode = NotificationWorkMode.PROD,
    var stubCase: NotificationStubType = NotificationStubType.NONE,

    var requestId: NotificationRequestId = NotificationRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    // Operations with notifications
    var notificationRequest: Notification = Notification(),
    var notificationResponse: Notification = Notification(),

    // Search for notifications
    var notificationFilterRequest: NotificationFilter = NotificationFilter(),
    var notificationFilterResponse: MutableList<Notification> = mutableListOf(),
)
