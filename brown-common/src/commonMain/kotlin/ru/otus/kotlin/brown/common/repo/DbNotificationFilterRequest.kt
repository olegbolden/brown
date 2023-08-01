package ru.otus.kotlin.brown.common.repo

import ru.otus.kotlin.brown.common.models.NotificationType
import ru.otus.kotlin.brown.common.models.NotificationUserId

data class DbNotificationFilterRequest(
    val titleFilter: String = "",
    val ownerId: NotificationUserId = NotificationUserId.NONE,
    val type: NotificationType? = null,
)
