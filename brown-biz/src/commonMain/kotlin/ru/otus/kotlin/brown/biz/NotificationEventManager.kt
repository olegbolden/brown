package ru.otus.kotlin.brown.biz

import kotlinx.datetime.Clock
import kotlinx.coroutines.channels.Channel
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext

object NotificationEventManager {
    private val eventChannel: Channel<Collection<NotificationEvent>> = Channel(
        capacity = Channel.UNLIMITED
    )

    fun getChannel(): Channel<Collection<NotificationEvent>> = eventChannel

    suspend fun processEvent(ctx: NotificationContext) {
        if (ctx.command == NotificationCommand.CREATE) {
            val event = NotificationEvent(
                id = ctx.notificationRepoDone.id,
                status = NotificationEventStatus.OPEN,
                type = ctx.requestNotification.type,
                createdAt = Clock.System.now()
            )
            NotificationEventState.addEvent(event)
            eventChannel.send(NotificationEventState.getNotificationEventList())
        }

        if (ctx.command == NotificationCommand.UPDATE) {
            val event = NotificationEvent(
                id = ctx.notificationRepoDone.id,
                status = if (ctx.notificationRepoDone.status == NotificationStatus.CLOSED) NotificationEventStatus.CLOSED else NotificationEventStatus.UPDATED,
                type = ctx.notificationRepoDone.type,
                createdAt = Clock.System.now()
            )
            NotificationEventState.addEvent(event)
            eventChannel.send(NotificationEventState.getNotificationEventList())
        }
    }
}
