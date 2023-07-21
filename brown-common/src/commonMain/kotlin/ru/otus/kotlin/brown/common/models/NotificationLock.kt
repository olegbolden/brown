package ru.otus.kotlin.brown.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class NotificationLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = NotificationLock("")
    }
}