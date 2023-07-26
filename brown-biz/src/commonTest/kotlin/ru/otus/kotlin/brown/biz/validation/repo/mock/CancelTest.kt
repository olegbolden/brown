package ru.otus.kotlin.brown.biz.validation.repo.mock

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.otus.kotlin.brown.common.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.repo.DbNotificationResponse
import ru.otus.kotlin.brown.repo.tests.NotificationRepositoryMock

@OptIn(ExperimentalCoroutinesApi::class)
class CancelTest {

    private val userId = NotificationUserId("321")
    private val command = NotificationCommand.CANCEL
    private val initNotification = Notification(
        id = NotificationId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        notificationType = NotificationType.COMMON,
        visibility = NotificationVisibility.PUBLIC,
        lock = NotificationLock("123-234-abc-ABC"),
    )
    private val repo by lazy {
        NotificationRepositoryMock(
            invokeReadNotification = {
               DbNotificationResponse(
                   isSuccess = true,
                   data = initNotification,
               )
            },
            invokeCancelNotification = {
                if (it.id == initNotification.id)
                    DbNotificationResponse(
                        isSuccess = true,
                        data = initNotification
                    )
                else DbNotificationResponse(isSuccess = false, data = null)
            }
        )
    }
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { NotificationProcessor(settings) }

    @Test
    fun repoCancelSuccessTest() = runTest {
        val notificationToUpdate = Notification(
            id = NotificationId("123"),
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
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initNotification.id, ctx.responseNotification.id)
        assertEquals(initNotification.title, ctx.responseNotification.title)
        assertEquals(initNotification.description, ctx.responseNotification.description)
        assertEquals(initNotification.notificationType, ctx.responseNotification.notificationType)
        assertEquals(initNotification.visibility, ctx.responseNotification.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
