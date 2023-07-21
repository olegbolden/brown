package ru.otus.kotlin.brown.repo.inmemory

import ru.otus.kotlin.brown.common.repo.INotificationRepository
import ru.otus.kotlin.brown.repo.tests.RepoNotificationUpdateTest

class NotificationUpdateTest : RepoNotificationUpdateTest() {
    override val repo: INotificationRepository = NotificationInMemoryRepo(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
