package ru.otus.kotlin.brown.biz.validation.repo.inmemory

import kotlin.test.Test
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.biz.validation.validators.*
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.models.NotificationCommand
import ru.otus.kotlin.brown.repo.inmemory.NotificationInMemoryRepo

class NotificationUpdateValidationTest {
    private val command = NotificationCommand.UPDATE
    private val processor by lazy { NotificationProcessor(CorSettings(repoTest = NotificationInMemoryRepo())) }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}
