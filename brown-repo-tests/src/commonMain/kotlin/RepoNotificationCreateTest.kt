package ru.otus.kotlin.brown.repo.tests

import kotlin.test.*
import ru.otus.kotlin.brown.common.repo.*
import ru.otus.kotlin.brown.common.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.NotificationType

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNotificationCreateTest {
    abstract val repo: INotificationRepository

    protected open val lockNew: NotificationLock = NotificationLock("20000000-0000-0000-0000-000000000002")

    private val createObj = Notification(
        title = "create object",
        description = "create object description",
        ownerId = NotificationUserId("owner-123"),
        visibility = NotificationVisibility.PUBLIC,
        type = NotificationType.ALERT,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createNotification(DbNotificationRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: NotificationId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.type, result.data?.type)
        assertNotEquals(NotificationId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitNotifications("create") {
        override val initObjects: List<Notification> = emptyList()
    }
}
