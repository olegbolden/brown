package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.helpers.fail
import ru.otus.kotlin.brown.common.helpers.errorValidation

fun ICorChainDsl<NotificationContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { requestNotificationValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
