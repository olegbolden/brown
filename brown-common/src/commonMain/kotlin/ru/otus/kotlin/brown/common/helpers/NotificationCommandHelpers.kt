package ru.otus.kotlin.brown.common.helpers

import ru.otus.kotlin.brown.common.models.NotificationCommand

fun NotificationCommand.isUpdatableCommand() =
    this in listOf(
        NotificationCommand.CREATE,
        NotificationCommand.UPDATE,
        NotificationCommand.CANCEL
    )
