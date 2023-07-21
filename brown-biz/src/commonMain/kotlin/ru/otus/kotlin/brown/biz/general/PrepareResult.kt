package ru.otus.kotlin.brown.biz.general

import ru.otus.kotlin.brown.cor.worker
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext

fun ICorChainDsl<NotificationContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != NotificationWorkMode.STUB }
    handle {
        responseNotification = notificationRepoDone
        responseNotificationList = notificationListRepoDone
        state = when (val st = state) {
            NotificationState.RUNNING -> NotificationState.FINISHING
            else -> st
        }
    }
}
