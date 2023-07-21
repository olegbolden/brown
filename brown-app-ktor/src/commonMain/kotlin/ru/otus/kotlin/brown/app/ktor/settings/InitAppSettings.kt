package ru.otus.kotlin.brown.app.ktor.settings

import io.ktor.server.application.*
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.log.common.LoggerProvider
import ru.otus.kotlin.brown.repo.inmemory.NotificationInMemoryRepo

fun Application.initAppSettings(): AppSettings {
    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = NotificationInMemoryRepo(),
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = NotificationProcessor(corSettings),
    )
}

expect fun Application.getLoggerProviderConf(): LoggerProvider
