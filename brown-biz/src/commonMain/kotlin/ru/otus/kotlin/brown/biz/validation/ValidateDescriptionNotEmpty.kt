package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.worker
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.helpers.*
import ru.otus.kotlin.brown.common.NotificationContext

fun ICorChainDsl<NotificationContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { requestNotificationValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
