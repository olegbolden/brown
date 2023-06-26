package ru.otus.kotlin.brown.biz.validation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.NotificationContext

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSearchTest {

    private val command = NotificationCommand.SEARCH
    private val processor by lazy { NotificationProcessor() }

    @Test
    fun correctEmpty() = runTest {
        val ctx = NotificationContext(
            command = command,
            state = NotificationState.NONE,
            workMode = NotificationWorkMode.TEST,
            requestNotificationFilter = NotificationFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(NotificationState.FAILING, ctx.state)
    }
}
