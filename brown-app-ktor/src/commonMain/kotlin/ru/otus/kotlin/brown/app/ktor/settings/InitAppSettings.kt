package ru.otus.kotlin.brown.app.ktor.settings

import io.ktor.server.application.*
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.log.common.LoggerProvider

fun Application.initAppSettings(): AppSettings = AppSettings(
    appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
    ),
    processor = NotificationProcessor(),
)

expect fun Application.getLoggerProviderConf(): LoggerProvider
