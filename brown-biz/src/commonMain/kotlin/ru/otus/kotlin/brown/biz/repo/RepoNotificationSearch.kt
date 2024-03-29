package ru.otus.kotlin.brown.biz.repo

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.repo.DbNotificationFilterRequest

fun ICorChainDsl<NotificationContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Search notifications from DB by filter"
    on { state == NotificationState.RUNNING }
    handle {
        val request = DbNotificationFilterRequest(
            titleFilter = requestNotificationFilterValidated.searchString,
            ownerId = requestNotificationFilterValidated.ownerId,
            type = requestNotificationFilterValidated.type,
        )
        val response = notificationRepo.searchNotification(request)
        val responseNotificationList = response.data
        if (response.isSuccess && responseNotificationList != null) {
            notificationListRepoDone = responseNotificationList.toMutableList()
        } else {
            state = NotificationState.FAILING
            errors.addAll(response.errors)
        }
    }
}
