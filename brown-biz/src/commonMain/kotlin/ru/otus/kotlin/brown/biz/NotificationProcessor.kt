package ru.otus.kotlin.brown.biz

import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.biz.ProcessingWorkflow.Companion.stubBusinessChain

class NotificationProcessor(val settings: CorSettings) : ProcessingWorkflow {
    suspend fun exec(ctx: NotificationContext) = stubBusinessChain.exec(ctx.apply { this.settings = this@NotificationProcessor.settings })
}
