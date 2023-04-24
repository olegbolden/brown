package ru.otus.kotlin.brown.common.helpers

import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationCommand

fun NotificationContext.isUpdatableCommand() =
    this.command in listOf(NotificationCommand.CREATE, NotificationCommand.UPDATE, NotificationCommand.CANCEL)
