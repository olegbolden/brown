package ru.otus.kotlin.brown.biz.validation.repo.stub

import kotlin.test.Test
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.biz.validation.validators.validationIdCorrect
import ru.otus.kotlin.brown.biz.validation.validators.validationIdEmpty
import ru.otus.kotlin.brown.biz.validation.validators.validationIdFormat
import ru.otus.kotlin.brown.biz.validation.validators.validationIdTrim
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.repo.stubs.NotificationStubRepo
import ru.otus.kotlin.brown.common.models.NotificationCommand

class NotificationReadValidationTest {
    private val command = NotificationCommand.READ
    private val processor by lazy { NotificationProcessor(CorSettings(repoTest = NotificationStubRepo())) }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}
