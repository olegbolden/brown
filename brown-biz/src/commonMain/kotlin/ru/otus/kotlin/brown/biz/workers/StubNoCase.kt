package ru.otus.kotlin.brown.biz.workers

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.helpers.fail
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.*

fun ICorChainDsl<NotificationContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == NotificationState.RUNNING }
    handle {
        fail(
            NotificationError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
