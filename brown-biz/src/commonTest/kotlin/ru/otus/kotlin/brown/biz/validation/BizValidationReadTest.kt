package ru.otus.kotlin.brown.biz.validation

import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.common.models.NotificationCommand

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {
    private val command = NotificationCommand.READ
    private val processor by lazy { NotificationProcessor() }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}
