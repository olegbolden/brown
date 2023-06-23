package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.chain
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState

fun ICorChainDsl<NotificationContext>.validation(block: ICorChainDsl<NotificationContext>.() -> Unit) = chain {
    block()
    title = "Validation"

    on { state == NotificationState.RUNNING }
}
