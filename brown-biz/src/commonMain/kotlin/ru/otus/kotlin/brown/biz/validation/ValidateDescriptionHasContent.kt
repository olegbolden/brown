package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.worker
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.helpers.*
import ru.otus.kotlin.brown.common.NotificationContext

// TODO-validation-7: example of error processing within business chain
fun ICorChainDsl<NotificationContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { requestNotificationValidating.description.isNotEmpty() && !requestNotificationValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
