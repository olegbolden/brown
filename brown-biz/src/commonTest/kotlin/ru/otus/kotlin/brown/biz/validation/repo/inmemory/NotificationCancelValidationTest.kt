package ru.otus.kotlin.brown.biz.validation.repo.inmemory

import kotlin.test.*
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.biz.validation.validators.validationIdCorrect
import ru.otus.kotlin.brown.biz.validation.validators.validationIdEmpty
import ru.otus.kotlin.brown.biz.validation.validators.validationIdFormat
import ru.otus.kotlin.brown.biz.validation.validators.validationIdTrim
import ru.otus.kotlin.brown.common.models.NotificationCommand
import ru.otus.kotlin.brown.repo.inmemory.NotificationInMemoryRepo

class NotificationCancelValidationTest {
    private val command = NotificationCommand.CANCEL
    private val processor by lazy { NotificationProcessor(CorSettings(repoTest = NotificationInMemoryRepo())) }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}
