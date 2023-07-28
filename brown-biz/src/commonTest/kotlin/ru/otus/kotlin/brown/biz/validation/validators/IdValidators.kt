package ru.otus.kotlin.brown.biz.validation.validators

import kotlin.test.assertEquals
import kotlin.test.assertContains
import kotlin.test.assertNotEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.*

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdCorrect(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = ru.otus.kotlin.brown.stubs.NotificationStub.prepareResult {
            lock = NotificationLock("123-234-abc-ABC")
        }
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotificationState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdTrim(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = "abc",
            type = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock("123-234-abc-ABC")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotificationState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId(""),
            title = "abc",
            description = "abc",
            type = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock("123-234-abc-ABC")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdFormat(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            type = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = NotificationLock("123-234-abc-ABC")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
