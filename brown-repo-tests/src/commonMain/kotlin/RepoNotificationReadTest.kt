package ru.otus.kotlin.brown.repo.tests

import kotlin.test.*
import ru.otus.kotlin.brown.common.repo.*
import ru.otus.kotlin.brown.common.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNotificationReadTest {
    abstract val repo: INotificationRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readNotification(DbNotificationIdRequest(readSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readNotification(DbNotificationIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitNotifications("read") {
        override val initObjects: List<Notification> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = NotificationId("notification-repo-read-notFound")

    }
}
