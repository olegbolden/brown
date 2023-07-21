package ru.otus.kotlin.brown.biz.workers

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

fun ICorChainDsl<NotificationContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubType == NotificationStubType.DB_ERROR && state == NotificationState.RUNNING }
    handle {
        state = NotificationState.FAILING
        this.errors.add(
            NotificationError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
