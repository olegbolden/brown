package ru.otus.kotlin.brown.biz.validation

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
fun validationDescriptionCorrect(command: NotificationCommand, processor: NotificationProcessor) = runTest {
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
    assertEquals("abc", ctx.requestNotificationValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionTrim(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = stub.id,
            title = "abc",
            description = " \n\tabc \n\t",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotificationState.FAILING, ctx.state)
    assertEquals("abc", ctx.requestNotificationValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionEmpty(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = stub.id,
            title = "abc",
            description = "",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionSymbols(command: NotificationCommand, processor: NotificationProcessor) = runTest {
    val ctx = NotificationContext(
        command = command,
        state = NotificationState.NONE,
        workMode = NotificationWorkMode.TEST,
        requestNotification = Notification(
            id = stub.id,
            title = "abc",
            description = "!@#$%^&*(),.{}",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotificationState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
