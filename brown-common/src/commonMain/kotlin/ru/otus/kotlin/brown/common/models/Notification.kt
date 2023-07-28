package ru.otus.kotlin.brown.common.models

data class Notification(
    var id: NotificationId = NotificationId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: NotificationUserId = NotificationUserId.NONE,
    var status: NotificationStatus = NotificationStatus.OPEN,
    var type: NotificationType = NotificationType.COMMON,
    var visibility: NotificationVisibility = NotificationVisibility.PRIVATE,
    val permissionsClient: MutableSet<NotificationPermissionClient> = mutableSetOf(),
    var lock: NotificationLock = NotificationLock.NONE,
) {
    fun deepCopy(): Notification = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        val NONE = Notification()
    }
}