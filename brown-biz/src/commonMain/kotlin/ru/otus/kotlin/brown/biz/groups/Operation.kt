package ru.otus.kotlin.brown.biz.groups

import ru.otus.kotlin.brown.cor.chain
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext

fun ICorChainDsl<NotificationContext>.operation(title: String, command: NotificationCommand, block: ICorChainDsl<NotificationContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == NotificationState.RUNNING }
}
