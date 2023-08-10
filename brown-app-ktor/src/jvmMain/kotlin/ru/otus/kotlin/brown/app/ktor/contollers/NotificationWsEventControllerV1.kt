package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.kotlin.brown.api.v1.mappers.apiV1mapper
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings
import ru.otus.kotlin.brown.biz.NotificationEventManager
import ru.otus.kotlin.brown.common.models.NotificationEvent
import ru.otus.kotlin.brown.common.models.NotificationEventState

class NotificationWsEventControllerV1 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()
    private var error = false

    private suspend fun sendMessage(eventList: Collection<NotificationEvent>) {
        mutex.withLock {
            sessions.forEach { session ->
                session.send(Frame.Text(apiV1mapper.writeValueAsString(eventList)))
            }
        }
    }

    suspend fun handle(session: WebSocketSession, appSettings: AppSettings) {
        mutex.withLock {
            sessions.add(session)
        }

        val logger = appSettings.corSettings.loggerProvider.logger(NotificationWsEventControllerV1::class)

        // Send eventList to client on session start
        val currentEventList = NotificationEventState.getNotificationEventList()
        session.send(Frame.Text(apiV1mapper.writeValueAsString(currentEventList)))

        NotificationEventManager.getChannel().receiveAsFlow().collect { eventList ->
            try {
                sendMessage(eventList)
            } catch (e: CancellationException) {
                error = true
            } finally {
                if (error) {
                    error = false
                    mutex.withLock {
                        sessions.removeIf { it == session }
                    }
                    sendMessage(eventList)
                }
            }
        }
    }
}
