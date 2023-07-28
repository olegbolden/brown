package ru.otus.kotlin.brown.stubs

import ru.otus.kotlin.brown.common.models.*

object ThunderNotificationStub {
    val NOTIFICATION_ALERT: Notification
        get() = Notification(
            id = NotificationId("888"),
            title = "Намечается гроза",
            description = "В 2 часа ночи намечается гроза, просьба закрыть форточки",
            ownerId = NotificationUserId("513"),
            type = NotificationType.ALERT,
            status = NotificationStatus.OPEN,
            visibility = NotificationVisibility.PUBLIC,
            permissionsClient = mutableSetOf(
                NotificationPermissionClient.READ,
                NotificationPermissionClient.UPDATE,
                NotificationPermissionClient.MAKE_VISIBLE_PUBLIC,
            )
        )
    val NOTIFICATION_WARNING: Notification
        get() = NOTIFICATION_ALERT.copy(type = NotificationType.WARNING)
}
