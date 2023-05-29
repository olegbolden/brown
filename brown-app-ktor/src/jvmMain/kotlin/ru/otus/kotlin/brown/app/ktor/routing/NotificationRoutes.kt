package ru.otus.kotlin.brown.app.ktor.routing

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.app.ktor.contollers.*
import ru.otus.kotlin.brown.biz.NotificationProcessor

fun Route.v1Notification(processor: NotificationProcessor) {
    route("notification") {
        post("create") {
            call.processRequest<NotificationCreateRequest>(processor)
        }
        post("read") {
            call.processRequest<NotificationReadRequest>(processor)
        }
        post("update") {
            call.processRequest<NotificationUpdateRequest>(processor)
        }
        post("cancel") {
            call.processRequest<NotificationCancelRequest>(processor)
        }
        post("search") {
            call.processRequest<NotificationSearchRequest>(processor)
        }
    }
}
