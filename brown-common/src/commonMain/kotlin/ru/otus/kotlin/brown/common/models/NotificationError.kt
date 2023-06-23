package ru.otus.kotlin.brown.common.models

data class NotificationError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: Level = Level.ERROR,
) {
    enum class Level {
        INFO, WARN, ERROR, DEBUG, TRACE
    }
}
