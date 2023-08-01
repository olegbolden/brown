package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState

fun ICorChainDsl<NotificationContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Prepare saving notification to DB"
    on { state == NotificationState.RUNNING }
    handle {
        notificationRepoRead = requestNotificationValidated.deepCopy()
        notificationRepoPrepare = notificationRepoRead
    }
}
