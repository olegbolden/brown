package ru.otus.kotlin.brown.repo.tests

import ru.otus.kotlin.brown.common.models.*

abstract class BaseInitNotifications(val op: String): IInitObjects<Notification> {

    open val lockOld: NotificationLock = NotificationLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: NotificationLock = NotificationLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: NotificationUserId = NotificationUserId("owner-123"),
        notificationType: NotificationType = NotificationType.COMMON,
        lock: NotificationLock = lockOld,
    ) = Notification(
        id = NotificationId("notification-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = NotificationVisibility.PRIVATE,
        notificationType = notificationType,
        lock = lock,
    )
}
