package ru.otus.kotlin.brown.biz.workers

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

fun ICorChainDsl<NotificationContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubType == NotificationStubType.SUCCESS && state == NotificationState.RUNNING }
    handle {
        state = NotificationState.FINISHING
        responseNotificationList.addAll(NotificationStub.prepareSearchList(requestNotificationFilter.searchString, NotificationType.ALERT))
    }
}
