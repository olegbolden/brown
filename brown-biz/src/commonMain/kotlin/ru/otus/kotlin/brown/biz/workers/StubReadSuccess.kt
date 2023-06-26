package ru.otus.kotlin.brown.biz.workers

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

fun ICorChainDsl<NotificationContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == NotificationStubType.SUCCESS && state == NotificationState.RUNNING }
    handle {
        state = NotificationState.FINISHING
        val stub = NotificationStub.prepareResult {
            requestNotification.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        responseNotification = stub
    }
}
