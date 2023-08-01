package ru.otus.kotlin.brown.common.exceptions

import ru.otus.kotlin.brown.common.models.NotificationLock

class RepoConcurrencyException(expectedLock: NotificationLock, actualLock: NotificationLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
