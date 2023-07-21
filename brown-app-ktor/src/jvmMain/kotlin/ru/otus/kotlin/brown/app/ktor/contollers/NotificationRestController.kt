package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.kotlin.brown.mappers.*
import ru.otus.kotlin.brown.biz.process
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.log.mappers.toLog
import ru.otus.kotlin.brown.log.common.ILogWrapper
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings

suspend inline fun <reified T : IRequest, reified Q : IResponse> RoutingCall.processV1(
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
            respond(ctx.toTransport())
        },
        { logId ->
            toLog(logId)
        }
    )
}
