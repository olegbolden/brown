package ru.otus.kotlin.brown.repo.tests

import kotlin.test.*
import ru.otus.kotlin.brown.common.repo.*
import ru.otus.kotlin.brown.common.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNotificationCancelTest {
    abstract val repo: INotificationRepository
    protected open val cancelSucc = initObjects[0]
    protected open val cancelConc = initObjects[1]
    protected open val notFoundId = NotificationId("notification-repo-cancel-notFound")

    @Test
    fun cancelSuccess() = runRepoTest {
        val lockOld = cancelSucc.lock
        val result = repo.cancelNotification(DbNotificationIdRequest(cancelSucc.id, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun cancelNotFound() = runRepoTest {
        val result = repo.readNotification(DbNotificationIdRequest(notFoundId, lock = lockOld))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun cancelConcurrency() = runRepoTest {
        val lockOld = cancelSucc.lock
        val result = repo.cancelNotification(DbNotificationIdRequest(cancelConc.id, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitNotifications("cancel") {
        override val initObjects: List<Notification> = listOf(
            createInitTestModel("cancel"),
            createInitTestModel("cancelLock"),
        )
    }
}
