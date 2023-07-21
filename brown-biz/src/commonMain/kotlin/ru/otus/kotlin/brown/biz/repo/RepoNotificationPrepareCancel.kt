package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState

fun ICorChainDsl<NotificationContext>.repoPrepareCancel(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == NotificationState.RUNNING }
    handle {
        notificationRepoPrepare = requestNotificationValidated.deepCopy()
    }
}
