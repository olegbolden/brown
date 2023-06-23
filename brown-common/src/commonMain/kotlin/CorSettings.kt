package ru.otus.kotlin.brown.common

import ru.otus.kotlin.brown.log.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
