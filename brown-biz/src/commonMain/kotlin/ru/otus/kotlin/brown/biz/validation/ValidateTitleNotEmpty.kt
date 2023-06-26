package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.worker
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.helpers.*
import ru.otus.kotlin.brown.common.NotificationContext

// TODO-validation-4: see an example of COR DSL validation
fun ICorChainDsl<NotificationContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { requestNotificationValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
