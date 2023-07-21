package ru.otus.kotlin.brown.common.repo

import ru.otus.kotlin.brown.common.models.*

data class DbNotificationIdRequest(
    val id: NotificationId,
    val lock: NotificationLock = NotificationLock.NONE,
) {
    constructor(notification: Notification): this(notification.id, notification.lock)
}
