package ru.otus.kotlin.brown.app.ktor.settings

import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.CorSettings

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: CorSettings,
    val processor: NotificationProcessor = NotificationProcessor(settings = corSettings)
)
