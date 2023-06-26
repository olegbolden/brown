package ru.otus.kotlin.brown.biz.workers

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

fun ICorChainDsl<NotificationContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    on { stubCase == NotificationStubType.BAD_DESCRIPTION && state == NotificationState.RUNNING }
    handle {
        state = NotificationState.FAILING
        this.errors.add(
            NotificationError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
