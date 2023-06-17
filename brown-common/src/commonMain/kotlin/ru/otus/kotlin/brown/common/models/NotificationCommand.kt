package ru.otus.kotlin.brown.common.models

import ru.otus.kotlin.brown.common.exceptions.*

enum class NotificationCommand {
    NONE,
    CREATE,
    READ,
    UPDATE,
    CANCEL,
    SEARCH;

    /**
     * Request type unambiguously derived from command
     */
    fun getRequestType()  = this.takeIf { it != NONE }?.name?.lowercase()
        ?: throw WrongRequestType()

    /**
     * Response type unambiguously derived from command
     */
    fun getResponseType()  = this.takeIf { it != NONE }?.name?.lowercase()
        ?: throw WrongResponseType()
}
