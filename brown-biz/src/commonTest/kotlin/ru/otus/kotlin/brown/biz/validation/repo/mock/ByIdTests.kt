package ru.otus.kotlin.brown.biz.validation.repo.mock

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.repo.DbNotificationResponse
import ru.otus.kotlin.brown.repo.tests.NotificationRepositoryMock

private val initNotification = Notification(
    id = NotificationId("123"),
    title = "abc",
    description = "abc",
    type = NotificationType.COMMON,
    visibility = NotificationVisibility.PUBLIC,
)
private val repo = NotificationRepositoryMock(
        invokeReadNotification = {
            if (it.id == initNotification.id) {
                DbNotificationResponse(
                    isSuccess = true,
                    data = initNotification,
                )
            } else DbNotificationResponse(
                isSuccess = false,
                data = null,
                errors = listOf(NotificationError(message = "Not found", field = "id"))
            )
        }
    )
private val settings by lazy {
    CorSettings(
        repoTest = repo
    )
}
private val processor by lazy { NotificationProcessor(settings) }

@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(command: NotificationCommand) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId("12345"),
            title = "xyz",
            description = "xyz",
            type = NotificationType.COMMON,
            visibility = NotificationVisibility.PRIVATE,
            lock = NotificationLock("123-234-abc-ABC"),

        ),
    )
    processor.exec(ctx)
    assertEquals(NotificationState.FAILING, ctx.state)
    assertEquals(Notification(), ctx.responseNotification)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
