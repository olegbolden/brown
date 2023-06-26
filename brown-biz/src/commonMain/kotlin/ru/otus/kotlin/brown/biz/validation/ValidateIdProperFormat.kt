package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.worker
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.helpers.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationId

fun ICorChainDsl<NotificationContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Consider moving this to NotificationId to implement various formats
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { requestNotificationValidating.id != NotificationId.NONE && !requestNotificationValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = requestNotificationValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
