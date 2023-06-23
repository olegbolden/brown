package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import ru.otus.kotlin.brown.mappers.*
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.log.common.ILogWrapper
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings

suspend inline fun <reified T : IRequest, reified Q: IResponse> ApplicationCall.processV1(
    appSettings: AppSettings,
    logger: ILogWrapper,
) {
    appSettings.processor.process<Q>(logger,
        { ctx ->
            val request = receive<T>()
            ctx.fromTransport(request)
        })
        { ctx ->
            respond(ctx.toTransport())
        }
}
