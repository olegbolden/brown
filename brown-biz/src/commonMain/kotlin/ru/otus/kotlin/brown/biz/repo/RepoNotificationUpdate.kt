package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.repo.DbNotificationRequest

fun ICorChainDsl<NotificationContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == NotificationState.RUNNING }
    handle {
        val request = DbNotificationRequest(notificationRepoPrepare)
        val response = notificationRepo.updateNotification(request)
        val resultNotification = response.data
        if (response.isSuccess && resultNotification != null) {
            notificationRepoDone = resultNotification
        } else {
            state = NotificationState.FAILING
            errors.addAll(response.errors)
            notificationRepoDone
        }
    }
}
