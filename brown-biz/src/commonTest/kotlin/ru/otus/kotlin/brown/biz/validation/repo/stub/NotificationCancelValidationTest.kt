package ru.otus.kotlin.brown.biz.validation.repo.stub

import kotlin.test.*
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.biz.validation.validators.*
import ru.otus.kotlin.brown.repo.stubs.NotificationStubRepo
import ru.otus.kotlin.brown.common.models.NotificationCommand

class NotificationCancelValidationTest {
    private val command = NotificationCommand.CANCEL
    private val processor = NotificationProcessor(CorSettings(repoTest = NotificationStubRepo()))

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)

    @Test
    fun correctLock() = validationLockCorrect(command, processor)
    @Test
    fun trimLock() = validationLockTrim(command, processor)
    @Test
    fun emptyLock() = validationLockEmpty(command, processor)
    @Test
    fun badFormatLock() = validationLockFormat(command, processor)
}
