package ru.otus.kotlin.brown.app.ktor.contollers

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import ru.otus.kotlin.brown.api.v1.mappers.apiV1Mapper
import ru.otus.kotlin.brown.api.v1.models.IRequest
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.helpers.addError
import ru.otus.kotlin.brown.common.helpers.asNotificationError
import ru.otus.kotlin.brown.common.helpers.isUpdatableCommand
import ru.otus.kotlin.brown.mappers.fromTransport
import ru.otus.kotlin.brown.mappers.toTransport
import ru.otus.kotlin.brown.mappers.toTransportInit
import ru.otus.kotlin.brown.stubs.NotificationStub

private val mutex = Mutex()
private val sessions = mutableSetOf<WebSocketSession>()

suspend fun WebSocketSession.wsHandlerV1(processor: NotificationProcessor) {
    mutex.withLock {
        sessions.add(this)
    }

    // Handle init request
    val ctx = NotificationContext()
    val init = apiV1Mapper.writeValueAsString(ctx.toTransportInit())
    outgoing.send(Frame.Text(init))

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull

        val jsonStr = frame.readText()
        val context = NotificationContext()

        // Handle without flow destruction
        try {
            val request = apiV1Mapper.readValue<IRequest>(jsonStr)
            context.fromTransport(request)
            processor.exec(context)

            val result = apiV1Mapper.writeValueAsString(context.toTransport())

            // If change request, response is sent to everyone
            if (context.command.isUpdatableCommand()) {
                mutex.withLock {
                    sessions.forEach {
                        it.send(Frame.Text(result))
                    }
                }
            } else {
                outgoing.send(Frame.Text(result))
            }
        } catch (_: ClosedReceiveChannelException) {
            mutex.withLock {
                sessions.clear()
            }
        } catch (t: Throwable) {
            context.addError(t.asNotificationError())

            val result = apiV1Mapper.writeValueAsString(context.toTransportInit())
            outgoing.send(Frame.Text(result))
        }
    }.collect()
}
