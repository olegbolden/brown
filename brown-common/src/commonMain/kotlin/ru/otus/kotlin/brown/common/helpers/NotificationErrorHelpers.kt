package ru.otus.kotlin.brown.common.helpers

import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationError
import ru.otus.kotlin.brown.common.models.NotificationState

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

fun NotificationContext.fail(error: NotificationError) {
    addError(error)
    state = NotificationState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Code that describes error. Shouldn't include field name or notice about validation.
     * For example: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: NotificationError.Level = NotificationError.Level.ERROR,
) = NotificationError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)
