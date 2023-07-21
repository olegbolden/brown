package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState

fun ICorChainDsl<NotificationContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == NotificationState.RUNNING }
    handle {
        notificationRepoPrepare = notificationRepoRead.deepCopy().apply {
            this.title = requestNotificationValidated.title
            description = requestNotificationValidated.description
            notificationType = requestNotificationValidated.notificationType
            visibility = requestNotificationValidated.visibility
        }
    }
}
