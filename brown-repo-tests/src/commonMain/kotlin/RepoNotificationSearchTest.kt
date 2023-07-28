package ru.otus.kotlin.brown.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.repo.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNotificationSearchTest {
    abstract val repo: INotificationRepository

    protected open val initializedObjects: List<Notification> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchNotification(DbNotificationFilterRequest(ownerId = searchOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchRequestType() = runRepoTest {
        val result = repo.searchNotification(DbNotificationFilterRequest(type = NotificationType.ALERT))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitNotifications("search") {

        val searchOwnerId = NotificationUserId("owner-124")
        override val initObjects: List<Notification> = listOf(
            createInitTestModel("notification1"),
            createInitTestModel("notification2", ownerId = searchOwnerId),
            createInitTestModel("notification3", type = NotificationType.ALERT),
            createInitTestModel("notification4", ownerId = searchOwnerId),
            createInitTestModel("notification5", type = NotificationType.ALERT),
        )
    }
}
