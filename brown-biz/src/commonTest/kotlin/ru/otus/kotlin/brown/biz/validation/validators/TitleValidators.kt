package ru.otus.kotlin.brown.biz.validation.validators

import kotlin.test.assertEquals
import kotlin.test.assertContains
import kotlin.test.assertNotEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.NotificationContext

private val stub = NotificationStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleCorrect(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = stub.id,
            title = "abc",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotificationState.FAILING, ctx.state)
    assertEquals("abc", ctx.requestNotificationValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotificationState.FAILING, ctx.state)
    assertEquals("abc", ctx.requestNotificationValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = stub.id,
            title = "",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = NotificationId("123"),
            title = "!@#$%^&*(),.{}",
            description = "abc",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
