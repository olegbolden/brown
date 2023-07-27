package ru.otus.kotlin.brown.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class NotificationId(private val id: String) {
    fun asString() = id
    fun asInt() = id.toInt()

    companion object {
        val NONE = NotificationId("")
    }
}
