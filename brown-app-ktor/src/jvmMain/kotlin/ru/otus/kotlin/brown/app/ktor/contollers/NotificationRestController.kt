package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.kotlin.brown.api.v1.models.IRequest
import ru.otus.kotlin.brown.api.v1.models.IResponse
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings
import ru.otus.kotlin.brown.biz.NotificationEventManager
import ru.otus.kotlin.brown.biz.process
import ru.otus.kotlin.brown.log.common.ILogWrapper
import ru.otus.kotlin.brown.log.mappers.toLog
import ru.otus.kotlin.brown.mappers.fromTransport
import ru.otus.kotlin.brown.mappers.toTransport

suspend inline fun <reified T : IRequest, reified Q : IResponse> ApplicationCall.processV1(
    appSettings: AppSettings,
    logger: ILogWrapper,
    loggingId: String = ""
) {
    appSettings.processor.process<Q>(
        logger, logId = loggingId,
        { ctx ->
            val request = receive<T>()
            ctx.fromTransport(request)
        },
        { ctx ->
            NotificationEventManager.processEvent(ctx)
            respond(ctx.toTransport())
        },
        { logId ->
            toLog(logId)
        }
    )
}
