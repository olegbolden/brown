package ru.otus.kotlin.brown.repo.inmemory

import ru.otus.kotlin.brown.common.repo.INotificationRepository
import ru.otus.kotlin.brown.repo.tests.RepoNotificationCancelTest

class NotificationCancelTest : RepoNotificationCancelTest() {
    override val repo: INotificationRepository = NotificationInMemoryRepo(
        initObjects = initObjects
    )
}
