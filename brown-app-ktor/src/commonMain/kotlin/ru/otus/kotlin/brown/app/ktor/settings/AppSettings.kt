package ru.otus.kotlin.brown.app.ktor.settings

import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor

data class AppSettings(
    val appUrls: List<String>,
    val corSettings: CorSettings,
    val processor: NotificationProcessor,
)
