package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.repo.DbNotificationIdRequest

fun ICorChainDsl<NotificationContext>.repoCancel(title: String) = worker {
    this.title = title
    description = "Cancellation of notification at DB by ID"
    on { state == NotificationState.RUNNING }
    handle {
        val request = DbNotificationIdRequest(notificationRepoPrepare)
        val response = notificationRepo.cancelNotification(request)
        if (!response.isSuccess) {
            state = NotificationState.FAILING
            errors.addAll(response.errors)
        }
        notificationRepoDone = notificationRepoRead
    }
}
