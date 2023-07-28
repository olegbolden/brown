package ru.otus.kotlin.brown.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationReadStubTest {

    private val processor = NotificationProcessor(CorSettings())
    val id = NotificationId("666")

    @Test
    fun read() = runTest {

        val ctx = NotificationContext(
            command = NotificationCommand.READ,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.SUCCESS,
            requestNotification = Notification(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (NotificationStub.get()) {
            assertEquals(id, ctx.responseNotification.id)
            assertEquals(title, ctx.responseNotification.title)
            assertEquals(description, ctx.responseNotification.description)
            assertEquals(type, ctx.responseNotification.type)
            assertEquals(visibility, ctx.responseNotification.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.READ,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_ID,
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
            command = NotificationCommand.READ,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.DB_ERROR,
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
            command = NotificationCommand.READ,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_TITLE,
            requestNotification = Notification(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
