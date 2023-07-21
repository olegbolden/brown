package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.repo.DbNotificationIdRequest

fun ICorChainDsl<NotificationContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Reading notification from DB"
    on { state == NotificationState.RUNNING }
    handle {
        val request = DbNotificationIdRequest(requestNotificationValidated)
        val response = notificationRepo.readNotification(request)
        val responseNotification = response.data
        if (response.isSuccess && responseNotification != null) {
            notificationRepoRead = responseNotification
        } else {
            state = NotificationState.FAILING
            errors.addAll(response.errors)
        }
    }
}
