package ru.otus.kotlin.brown.biz.workers

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState

fun ICorChainDsl<NotificationContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == NotificationState.NONE }
    handle { state = NotificationState.RUNNING }
}
