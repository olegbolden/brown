package ru.otus.kotlin.brown.app.ktor.routing

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.serialization.jackson.*
import ru.otus.kotlin.brown.api.v1.mappers.*
import io.ktor.server.plugins.contentnegotiation.*
import ru.otus.kotlin.brown.app.ktor.contollers.*
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings

fun Application.configureRouting(appSettings: AppSettings) {

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1mapper.serializationConfig)
                    setConfig(apiV1mapper.deserializationConfig)
                }
            }

            v1Notification(appSettings)
        }

        webSocket("/ws/v1") {
            NotificationWsControllerV1().handle(this, appSettings)
        }
    }
}
