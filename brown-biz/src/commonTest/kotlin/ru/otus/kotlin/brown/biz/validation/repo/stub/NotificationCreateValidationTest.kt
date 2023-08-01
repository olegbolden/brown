package ru.otus.kotlin.brown.biz.validation.repo.stub

import kotlin.test.Test
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.biz.validation.validators.*
import ru.otus.kotlin.brown.repo.stubs.NotificationStubRepo
import ru.otus.kotlin.brown.common.models.NotificationCommand

class NotificationCreateValidationTest {
    private val command = NotificationCommand.CREATE
    private val processor by lazy { NotificationProcessor(CorSettings(repoTest = NotificationStubRepo())) }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)
}
