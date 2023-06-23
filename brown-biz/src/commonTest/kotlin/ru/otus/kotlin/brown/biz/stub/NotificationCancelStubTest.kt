package ru.otus.kotlin.brown.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationCancelStubTest {

    private val processor = NotificationProcessor()
    val id = NotificationId("666")

    @Test
    fun cancel() = runTest {

        val ctx = NotificationContext(
            command = NotificationCommand.CANCEL,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubCase = NotificationStubType.SUCCESS,
            requestNotification = Notification(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = NotificationStub.get()
        assertEquals(stub.id, ctx.responseNotification.id)
        assertEquals(stub.title, ctx.responseNotification.title)
        assertEquals(stub.description, ctx.responseNotification.description)
        assertEquals(stub.notificationType, ctx.responseNotification.notificationType)
        assertEquals(stub.visibility, ctx.responseNotification.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.CANCEL,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubCase = NotificationStubType.BAD_ID,
            requestNotification = Notification(),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.CANCEL,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubCase = NotificationStubType.DB_ERROR,
            requestNotification = Notification(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.CANCEL,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubCase = NotificationStubType.BAD_TITLE,
            requestNotification = Notification(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
