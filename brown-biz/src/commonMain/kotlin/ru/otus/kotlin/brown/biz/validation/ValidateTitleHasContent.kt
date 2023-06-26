package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.worker
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.helpers.*
import ru.otus.kotlin.brown.common.NotificationContext

fun ICorChainDsl<NotificationContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { requestNotificationValidating.title.isNotEmpty() && !requestNotificationValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain leters"
            )
        )
    }
}
