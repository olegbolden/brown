package ru.otus.kotlin.brown.app.ktor.routing

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.app.ktor.contollers.processV1
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings

fun RoutingBuilder.v1Notification(appSettings: AppSettings) {

    val logger = appSettings.corSettings.loggerProvider.logger(Route::v1Notification::class)

    route("notification") {
        post("create") {
            call.processV1<NotificationCreateRequest, NotificationCreateResponse>(appSettings, logger)
        }
        post("read") {
            call.processV1<NotificationReadRequest, NotificationReadResponse>(appSettings, logger)
        }
        post("update") {
            call.processV1<NotificationUpdateRequest, NotificationUpdateResponse>(appSettings, logger)
        }
        post("cancel") {
            call.processV1<NotificationCancelRequest, NotificationCancelResponse>(appSettings, logger)
        }
        post("search") {
            call.processV1<NotificationSearchRequest, NotificationSearchResponse>(appSettings, logger)
        }
    }
}
