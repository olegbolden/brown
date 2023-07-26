package ru.otus.kotlin.brown.biz.validation.repo.mock

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.biz.validation.repo.mock.repoNotFoundTest
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.repo.DbNotificationResponse
import ru.otus.kotlin.brown.repo.tests.NotificationRepositoryMock

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTest {

    private val userId = NotificationUserId("321")
    private val command = NotificationCommand.UPDATE
    private val initNotification = Notification(
        id = NotificationId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        notificationType = NotificationType.COMMON,
        visibility = NotificationVisibility.PUBLIC,
    )
    private val repo by lazy { NotificationRepositoryMock(
        invokeReadNotification = {
            DbNotificationResponse(
                isSuccess = true,
                data = initNotification,
            )
        },
        invokeUpdateNotification = {
            DbNotificationResponse(
                isSuccess = true,
                data = Notification(
                    id = NotificationId("123"),
                    title = "xyz",
                    description = "xyz",
                    notificationType = NotificationType.COMMON,
                    visibility = NotificationVisibility.PUBLIC,
                )
            )
        }
    ) }
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { NotificationProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val notificationToUpdate = Notification(
            id = NotificationId("123"),
            title = "xyz",
            description = "xyz",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock("123-234-abc-ABC"),
        )
        val ctx = NotificationContext(
            command = command,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.TEST,
            requestNotification = notificationToUpdate,
        )
        processor.exec(ctx)
        assertEquals(NotificationState.FINISHING, ctx.state)
        assertEquals(notificationToUpdate.id, ctx.responseNotification.id)
        assertEquals(notificationToUpdate.title, ctx.responseNotification.title)
        assertEquals(notificationToUpdate.description, ctx.responseNotification.description)
        assertEquals(notificationToUpdate.notificationType, ctx.responseNotification.notificationType)
        assertEquals(notificationToUpdate.visibility, ctx.responseNotification.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
