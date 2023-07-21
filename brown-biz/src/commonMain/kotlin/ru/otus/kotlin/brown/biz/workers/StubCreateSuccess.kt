package ru.otus.kotlin.brown.biz.workers

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

fun ICorChainDsl<NotificationContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubType == NotificationStubType.SUCCESS && state == NotificationState.RUNNING }
    handle {
        state = NotificationState.FINISHING
        responseNotification = NotificationStub.prepareResult {
            requestNotification.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            requestNotification.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            notificationType = requestNotification.notificationType
            visibility = requestNotification.visibility
        }
    }
}
