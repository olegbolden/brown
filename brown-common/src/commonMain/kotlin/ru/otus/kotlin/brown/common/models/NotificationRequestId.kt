package ru.otus.kotlin.brown.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class NotificationRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = NotificationRequestId("")
    }
}
