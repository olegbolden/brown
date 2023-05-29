package ru.otus.kotlin.brown.app.ktor.contollers

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import ru.otus.kotlin.brown.mappers.*
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.NotificationContext

suspend inline fun <reified T: IRequest> ApplicationCall.processRequest(processor: NotificationProcessor) {
    val request = receive<T>() as IRequest
    val context = NotificationContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransport())
}
