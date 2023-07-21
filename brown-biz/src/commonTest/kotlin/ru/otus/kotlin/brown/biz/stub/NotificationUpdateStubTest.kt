package ru.otus.kotlin.brown.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationUpdateStubTest {

    private val processor = NotificationProcessor(CorSettings())
    val id = NotificationId("777")
    val title = "title 666"
    val description = "desc 666"
    val dealSide = NotificationType.COMMON
    val visibility = NotificationVisibility.PUBLIC

    @Test
    fun create() = runTest {

        val ctx = NotificationContext(
            command = NotificationCommand.UPDATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.SUCCESS,
            requestNotification = Notification(
                id = id,
                title = title,
                description = description,
                notificationType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.responseNotification.id)
        assertEquals(title, ctx.responseNotification.title)
        assertEquals(description, ctx.responseNotification.description)
        assertEquals(dealSide, ctx.responseNotification.notificationType)
        assertEquals(visibility, ctx.responseNotification.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.UPDATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_TITLE,
            requestNotification = Notification(
                id = id,
                title = "",
                description = description,
                notificationType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.UPDATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_DESCRIPTION,
            requestNotification = Notification(
                id = id,
                title = title,
                description = "",
                notificationType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.UPDATE,
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
            command = NotificationCommand.UPDATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_SEARCH_STRING,
            requestNotification = Notification(
                id = id,
                title = title,
                description = description,
                notificationType = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
