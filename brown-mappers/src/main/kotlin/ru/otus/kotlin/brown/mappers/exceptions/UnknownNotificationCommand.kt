package ru.otus.kotlin.brown.mappers.exceptions

import ru.otus.kotlin.brown.common.models.NotificationCommand

class UnknownNotificationCommand(command: NotificationCommand) : Throwable("Wrong command $command at mapping toTransport stage")
