package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.websocket.*
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.kotlin.brown.api.v1.mappers.apiV1mapper
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings
import ru.otus.kotlin.brown.biz.NotificationEventManager
import ru.otus.kotlin.brown.common.models.NotificationEventState

class NotificationWsEventControllerV1 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, appSettings: AppSettings) {
        mutex.withLock {
            sessions.add(session)
        }

        val logger = appSettings.corSettings.loggerProvider.logger(NotificationWsEventControllerV1::class)

        // Send eventList to client on session start
        val currentEventList = NotificationEventState.getNotificationEventList()
        session.send(Frame.Text(apiV1mapper.writeValueAsString(currentEventList)))

        NotificationEventManager.getChannel().receiveAsFlow().collect { eventList ->
            println("----------------------------------------")
            println("Открытых сессий: ${sessions.size} ")
            println("----------------------------------------")
            try {
                mutex.withLock {
                    sessions.forEach { session ->
                        session.send(Frame.Text(apiV1mapper.writeValueAsString(eventList)))
                    }
                }
            } catch (error: Throwable) {
                println("----------------------------------------")
                println(error.message)
                println("----------------------------------------")
            } finally {
                mutex.withLock {
                    println("----------------------------------------")
                    println("Client closed!")
                    println("----------------------------------------")
                    sessions.removeIf { it == session }
                }
            }
        }
        // Handle without flow destruction
        session.incoming.receiveAsFlow().collect { it ->
            val frame = it as? Frame.Text ?: return@collect
            try {
                val text = frame.readText()
                println(text)
            } catch (error: Throwable) {
                println("----------------------------------------")
                println(error.message)
                println("----------------------------------------")
            } finally {
                mutex.withLock {
                    println("----------------------------------------")
                    println("Client closed!")
                    println("----------------------------------------")
                    sessions.removeIf { it == session }
                }
            }
        }
    }
}
