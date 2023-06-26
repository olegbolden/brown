package ru.otus.kotlin.brown.biz

import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.biz.ProcessingWorkflow.Companion.stubBusinessChain

class NotificationProcessor : ProcessingWorkflow {
    suspend fun exec(ctx: NotificationContext) = stubBusinessChain.exec(ctx)
}
