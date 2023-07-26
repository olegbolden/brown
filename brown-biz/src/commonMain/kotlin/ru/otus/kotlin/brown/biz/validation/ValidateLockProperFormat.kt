package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.helpers.fail
import ru.otus.kotlin.brown.common.helpers.errorValidation
import ru.otus.kotlin.brown.common.models.NotificationLock

fun ICorChainDsl<NotificationContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { requestNotificationValidating.lock != NotificationLock.NONE && !requestNotificationValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = requestNotificationValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
