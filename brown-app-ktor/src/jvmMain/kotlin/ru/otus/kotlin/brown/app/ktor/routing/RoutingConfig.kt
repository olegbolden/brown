package ru.otus.kotlin.brown.app.ktor.routing

import io.ktor.serialization.jackson.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.server.plugins.contentnegotiation.*
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.api.v1.mappers.apiV1Mapper
import ru.otus.kotlin.brown.app.ktor.contollers.wsHandlerV1

fun Application.configureRouting() {
    val processor = NotificationProcessor()

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }

            v1Notification(processor)
        }

        webSocket("/ws/v1") {
            wsHandlerV1(processor)
        }
    }
}
