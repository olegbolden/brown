package ru.otus.kotlin.brown.common.helpers

import ru.otus.kotlin.brown.common.models.NotificationCommand

fun NotificationCommand.isUpdatable() =
    this in listOf(
        NotificationCommand.CREATE,
        NotificationCommand.UPDATE,
        NotificationCommand.CANCEL
    )
