package ru.otus.kotlin.brown.repo.inmemory

import ru.otus.kotlin.brown.repo.tests.RepoNotificationCreateTest

class NotificationCreateTest : RepoNotificationCreateTest() {
    override val repo = NotificationInMemoryRepo(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}