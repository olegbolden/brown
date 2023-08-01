package ru.otus.kotlin.brown.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.stubs.NotificationStubType
import ru.otus.kotlin.brown.stubs.NotificationStub

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationCreateStubTest {

    private val processor = NotificationProcessor(CorSettings())
    val id = NotificationId("666")
    val title = "title 666"
    val description = "desc 666"
    val dealSide = NotificationType.COMMON
    val visibility = NotificationVisibility.PUBLIC

    @Test
    fun create() = runTest {

        val ctx = NotificationContext(
            command = NotificationCommand.CREATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.SUCCESS,
            requestNotification = Notification(
                id = id,
                title = title,
                description = description,
                type = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(NotificationStub.get().id, ctx.responseNotification.id)
        assertEquals(title, ctx.responseNotification.title)
        assertEquals(description, ctx.responseNotification.description)
        assertEquals(dealSide, ctx.responseNotification.type)
        assertEquals(visibility, ctx.responseNotification.visibility)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = NotificationContext(
            command = NotificationCommand.CREATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_TITLE,
            requestNotification = Notification(
                id = id,
                title = "",
                description = description,
                type = dealSide,
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
            command = NotificationCommand.CREATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_DESCRIPTION,
            requestNotification = Notification(
                id = id,
                title = title,
                description = "",
                type = dealSide,
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
            command = NotificationCommand.CREATE,
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
            command = NotificationCommand.CREATE,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.STUB,
            stubType = NotificationStubType.BAD_ID,
            requestNotification = Notification(
                id = id,
                title = title,
                description = description,
                type = dealSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(Notification(), ctx.responseNotification)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
