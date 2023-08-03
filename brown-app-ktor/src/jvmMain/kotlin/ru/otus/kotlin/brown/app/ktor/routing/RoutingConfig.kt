package ru.otus.kotlin.brown.app.ktor.routing

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import ru.otus.kotlin.brown.api.v1.mappers.apiV1mapper
import ru.otus.kotlin.brown.app.ktor.contollers.NotificationWsControllerV1
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings
import ru.otus.kotlin.brown.app.ktor.settings.initPlugins

fun Application.configureRouting(appSettings: AppSettings) {
    initPlugins(appSettings)

    routing {
        route("v1") {
            pluginRegistry.getOrNull(AttributeKey("ContentNegotiation"))?: install(ContentNegotiation) {
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

        staticResources("/", "public") {
            default("index.html")
        }
    }
}
