package ru.otus.kotlin.brown.repo.inmemory

import ru.otus.kotlin.brown.common.repo.INotificationRepository
import ru.otus.kotlin.brown.repo.tests.RepoNotificationReadTest

class NotificationReadTest: RepoNotificationReadTest() {
    override val repo: INotificationRepository = NotificationInMemoryRepo(
        initObjects = initObjects
    )
}
