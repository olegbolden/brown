package ru.otus.kotlin.brown.common.repo

import ru.otus.kotlin.brown.common.models.*

data class DbNotificationFilterRequest(
    val titleFilter: String = "",
    val ownerId: NotificationUserId = NotificationUserId.NONE,
    val notificationType: NotificationType? = null,
)
