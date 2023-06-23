package ru.otus.kotlin.brown.app.ktor.settings

import io.ktor.server.application.*
import ru.otus.kotlin.brown.log.common.LoggerProvider
import ru.otus.kotlin.brown.log.logback.logbackLogger

actual fun Application.getLoggerProviderConf(): LoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> LoggerProvider { logbackLogger(it) }
        else -> throw Exception("Logger $mode is not allowed. Allowed value is logback only")
}
