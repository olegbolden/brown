package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.kotlin.brown.api.v1.mappers.*
import ru.otus.kotlin.brown.api.v1.models.IRequest
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.helpers.addError
import ru.otus.kotlin.brown.mappers.fromTransport
import ru.otus.kotlin.brown.mappers.toTransport
import ru.otus.kotlin.brown.mappers.toTransportInit
import ru.otus.kotlin.brown.common.helpers.asNotificationError
import ru.otus.kotlin.brown.common.helpers.isUpdatableCommand
import kotlinx.coroutines.channels.ClosedReceiveChannelException

class NotificationWsControllerV1 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, processor: NotificationProcessor) {
        mutex.withLock {
            sessions.add(session)
        }

        // Handle init request
        val ctx = NotificationContext()
        val init = apiV1Serialize(ctx.toTransportInit())
        session.outgoing.send(Frame.Text(init))

        // Handle flow
        session.incoming.receiveAsFlow().mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull

            val jsonStr = frame.readText()
            val context = NotificationContext()

            // Handle without flow destruction
            try {
                val request = apiV1Deserialize<IRequest>(jsonStr)
                context.fromTransport(request)
                processor.exec(context)

                val result = apiV1Serialize(context.toTransport())

                // If change request, response is sent to everyone
                if (context.command.isUpdatableCommand()) {

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
            } catch (t: Throwable) {
                context.addError(t.asNotificationError())

                val result = apiV1Serialize(context.toTransportInit())
                session.outgoing.send(Frame.Text(result))
            }
        }.collect()
    }
}