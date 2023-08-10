package ru.otus.kotlin.brown.common.models

import kotlinx.datetime.Clock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.time.Duration.Companion.hours

/**
 * Список событий за последнее время (в часах до текущего момента)
 */
private val EVENT_MONITORING_PERIOD = 24.hours

object NotificationEventState {
    private val notificationEventList: MutableList<NotificationEvent> = mutableListOf()
    private val lock: ReentrantReadWriteLock = ReentrantReadWriteLock(true)
    private fun <T> lockWrapper(block: () -> T): T {
        lock.readLock().lock()
        try {
            return block()
        } finally {
            lock.readLock().unlock()
        }
    }

    /**
     * Возвращает список событий, происшедших в течение текущего дня
     */
    fun getNotificationEventList(): List<NotificationEvent> = lockWrapper {
        notificationEventList
    }

    /**
     * Добавляет событие
     */
    fun addEvent(notificationEvent: NotificationEvent) = lockWrapper {
        removeObsoleteEvents()
        notificationEventList.add(notificationEvent)
    }

    /**
     * Удаляет из списка события, выходящие за рамки указанного периода (EVENT_MONITORING_PERIOD)
     */
    private fun removeObsoleteEvents() = lockWrapper {
        val threshold = Clock.System.now().minus(EVENT_MONITORING_PERIOD)
        notificationEventList.removeIf { it.createdAt < threshold }
    }
}
