package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.repo.DbNotificationRequest

fun ICorChainDsl<NotificationContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Saving notification to DB"
    on { state == NotificationState.RUNNING }
    handle {
        val request = DbNotificationRequest(notificationRepoPrepare)
        val response = notificationRepo.createNotification(request)
        val responseNotification = response.data
        if (response.isSuccess && responseNotification != null) {
            notificationRepoDone = responseNotification
        } else {
            state = NotificationState.FAILING
            errors.addAll(response.errors)
        }
    }
}
