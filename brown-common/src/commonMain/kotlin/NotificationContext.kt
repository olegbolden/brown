package ru.otus.kotlin.brown.common

import kotlinx.datetime.Instant
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.models.NotificationFilter
import ru.otus.kotlin.brown.common.stubs.NotificationStubType
import ru.otus.kotlin.brown.common.repo.INotificationRepository

data class NotificationContext(
    var settings: CorSettings = CorSettings.NONE,

    var timeStart: Instant = Instant.NONE,
    var state: NotificationState = NotificationState.NONE,
    var command: NotificationCommand = NotificationCommand.NONE,

    var stubType: NotificationStubType = NotificationStubType.NONE,
    var workMode: NotificationWorkMode = NotificationWorkMode.PROD,

    // Request params
    var requestId: NotificationRequestId = NotificationRequestId.NONE,
    var requestNotification: Notification = Notification(),
    var requestNotificationFilter: NotificationFilter = NotificationFilter(),

    // Response params
    var responseNotification: Notification = Notification(),
    var responseNotificationList: MutableList<Notification> = mutableListOf(),

    // Validation
    var requestNotificationValidating: Notification = Notification(),
    var requestNotificationFilterValidating: NotificationFilter = NotificationFilter(),
    var requestNotificationValidated: Notification = Notification(),
    var requestNotificationFilterValidated: NotificationFilter = NotificationFilter(),

    // Repo staff
    var notificationRepo: INotificationRepository = INotificationRepository.NONE,
    var notificationRepoPrepare: Notification = Notification(),
    var notificationRepoRead: Notification = Notification(),
    var notificationRepoDone: Notification = Notification(),
    var notificationListRepoDone: MutableList<Notification> = mutableListOf(),

    // Errors
    val errors: MutableList<NotificationError> = mutableListOf(),
)
