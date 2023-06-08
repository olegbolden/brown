package ru.otus.kotlin.brown.biz

import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationType
import ru.otus.kotlin.brown.common.models.NotificationCommand
import ru.otus.kotlin.brown.common.models.NotificationState
import ru.otus.kotlin.brown.common.models.NotificationWorkMode

class NotificationProcessor {
    suspend fun exec(ctx: NotificationContext) {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == NotificationWorkMode.STUB) {
            "Currently working only in STUB mode."
        }

        // Set state for successful stubs
        ctx.state = NotificationState.FINISHING

        when (ctx.command) {
            NotificationCommand.SEARCH -> {
                ctx.notificationFilterResponse.addAll(NotificationStub.prepareSearchList(ctx.notificationFilterRequest.searchString, NotificationType.ALERT))
            }
            else -> {
                ctx.notificationResponse = NotificationStub.get()
            }
        }
    }
}
