package ru.otus.kotlin.brown.common.models

data class Notification(
    var id: NotificationId = NotificationId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: NotificationUserId = NotificationUserId.NONE,
    val notificationType: NotificationType = NotificationType.NONE,
    var visibility: NotificationVisibility = NotificationVisibility.NONE,
    val permissionsClient: MutableSet<NotificationPermissionClient> = mutableSetOf()
)
