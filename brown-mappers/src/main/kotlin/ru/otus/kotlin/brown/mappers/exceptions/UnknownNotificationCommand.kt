package ru.otus.kotlin.brown.mappers.exceptions

class UnknownNotificationCommand() : Throwable("Wrong command at mapping toTransport stage")
