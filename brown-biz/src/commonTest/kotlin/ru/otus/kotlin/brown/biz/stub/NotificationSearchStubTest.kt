package ru.otus.kotlin.brown.biz.stub

import kotlin.test.Test
import kotlin.test.fail
import kotlin.test.assertTrue
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
class NotificationSearchStubTest {

    private val processor = NotificationProcessor(CorSettings())
    private val filter = NotificationFilter(searchString = "alert")

    @Test
    fun read() = runTest {

        val ctx = NotificationContext(
            command = NotificationCommand.SEARCH,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.SUCCESS,
            requestNotificationFilter = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.responseNotificationList.size > 1)
        val first = ctx.responseNotificationList.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (NotificationStub.get()) {
            assertEquals(notificationType, first.notificationType)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.SEARCH,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_ID,
            requestNotificationFilter = filter,
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.SEARCH,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.DB_ERROR,
            requestNotificationFilter = filter,
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.SEARCH,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_TITLE,
            requestNotificationFilter = filter,
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
