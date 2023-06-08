package ru.otus.kotlin.brown.common.models

import ru.otus.kotlin.brown.common.exceptions.WrongRequestOrResponseType

enum class NotificationCommand {
    NONE,
    CREATE,
    READ,
    UPDATE,
    CANCEL,
    SEARCH;

    /**
     * Request||Response type unambiguously derived from command
     */
    fun getRequestResponseType() = this.takeIf { it != NONE }?.name?.lowercase()
            ?: throw WrongRequestOrResponseType()
}
