package ru.otus.kotlin.brown.biz.groups

import ru.otus.kotlin.brown.cor.chain
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.models.NotificationWorkMode

fun ICorChainDsl<NotificationContext>.stubs(title: String, block: ICorChainDsl<NotificationContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == NotificationWorkMode.STUB && state == NotificationState.RUNNING }
}
