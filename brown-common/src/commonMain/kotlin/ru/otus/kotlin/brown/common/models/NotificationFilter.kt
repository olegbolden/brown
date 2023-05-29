package ru.otus.kotlin.brown.common.models

data class NotificationFilter(
    var searchString: String = "",
    var notificationType: NotificationType? = null,
)
