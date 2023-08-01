package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.kotlin.brown.api.v1.mappers.*
import ru.otus.kotlin.brown.api.v1.models.IRequest
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.mappers.fromTransport
import ru.otus.kotlin.brown.mappers.toTransport
import ru.otus.kotlin.brown.mappers.toTransportInit
import ru.otus.kotlin.brown.common.helpers.isUpdatable
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import ru.otus.kotlin.brown.api.v1.models.IResponse
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings
import ru.otus.kotlin.brown.biz.process
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.log.mappers.toLog

class NotificationWsControllerV1 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, appSettings: AppSettings) {
        mutex.withLock {
            sessions.add(session)
        }

        val logger = appSettings.corSettings.loggerProvider.logger(NotificationWsControllerV1::class)

        // Handle init request
        val ctx = NotificationContext()
        val init = apiV1Serialize(ctx.toTransportInit())
        session.outgoing.send(Frame.Text(init))

        // Handle without flow destruction
        session.incoming.receiveAsFlow().mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull

            val jsonStr = frame.readText()
            appSettings.processor.process<IResponse>(logger, logId = "",
                { ctx ->
                    val request = apiV1Deserialize<IRequest>(jsonStr)
                    ctx.fromTransport(request)
                },
                { ctx ->
                    try {
                        val result = apiV1Serialize(ctx.toTransport())

                        // If this is request for mutation, response is sent to everyone
                        if (ctx.command.isUpdatable()) {
                            val message = Frame.Text(result)
                            mutex.withLock {
                                sessions.forEach { session ->
                                    session.send(message)
                                }
                            }
                        } else {
                            session.outgoing.send(Frame.Text(result))
                        }
                    } catch (_: ClosedReceiveChannelException) {
                        mutex.withLock {
                            sessions.removeIf { it == session }
                        }
                    }
                },
                { logId ->
                    toLog(logId)
                }
            )
        }.collect()
    }
}
