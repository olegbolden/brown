package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.cor.ICorChainDsl
import ru.otus.kotlin.brown.cor.worker

fun ICorChainDsl<NotificationContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == NotificationState.RUNNING }
    handle {
        notificationRepoPrepare = notificationRepoRead.deepCopy().apply {
            this.title = requestNotificationValidated.title
            description = requestNotificationValidated.description
            type = requestNotificationValidated.type
            visibility = requestNotificationValidated.visibility
            status = requestNotificationValidated.status
            lock = requestNotificationValidated.lock
        }
    }
}
