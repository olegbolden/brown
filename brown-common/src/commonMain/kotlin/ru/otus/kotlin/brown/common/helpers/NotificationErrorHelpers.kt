package ru.otus.kotlin.brown.common.helpers

import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationError

fun Throwable.asNotificationError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = NotificationError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun NotificationContext.addError(vararg error: NotificationError) = errors.addAll(error)
