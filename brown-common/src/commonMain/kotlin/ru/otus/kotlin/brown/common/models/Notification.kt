package ru.otus.kotlin.brown.common.models

data class Notification(
    var id: NotificationId = NotificationId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: NotificationUserId = NotificationUserId.NONE,
    var notificationType: NotificationType = NotificationType.COMMON,
    var visibility: NotificationVisibility = NotificationVisibility.PRIVATE,
    val permissionsClient: MutableSet<NotificationPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): Notification = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )
}