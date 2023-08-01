package ru.otus.kotlin.brown.biz.validation

import ru.otus.kotlin.brown.cor.worker
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState

fun ICorChainDsl<NotificationContext>.finishNotificationValidation(title: String) = worker {
    this.title = title
    on { state == NotificationState.RUNNING }
    handle {
        requestNotificationValidated = requestNotificationValidating
    }
}

fun ICorChainDsl<NotificationContext>.finishNotificationFilterValidation(title: String) = worker {
    this.title = title
    on { state == NotificationState.RUNNING }
    handle {
        requestNotificationFilterValidated = requestNotificationFilterValidating
    }
}
