package ru.otus.kotlin.brown.common

import ru.otus.kotlin.brown.log.common.LoggerProvider
import ru.otus.kotlin.brown.common.repo.INotificationRepository

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoTest: INotificationRepository = INotificationRepository.NONE,
    val repoProd: INotificationRepository = INotificationRepository.NONE,

    ) {
    companion object {
        val NONE = CorSettings()
    }
}
