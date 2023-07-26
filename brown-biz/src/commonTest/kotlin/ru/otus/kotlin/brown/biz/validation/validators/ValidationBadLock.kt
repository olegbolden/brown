package ru.otus.kotlin.brown.biz.validation.validators

import kotlin.test.assertEquals
import kotlin.test.assertContains
import kotlin.test.assertNotEquals
import kotlinx.coroutines.test.runTest
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.biz.NotificationProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.NotificationContext

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockCorrect(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotificationState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockTrim(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotificationState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockEmpty(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockFormat(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
