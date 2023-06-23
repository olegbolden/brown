package ru.otus.kotlin.brown.biz

import kotlinx.datetime.Clock
import ru.otus.kotlin.brown.api.v1.models.IResponse
import ru.otus.kotlin.brown.log.mappers.toLog
import ru.otus.kotlin.brown.log.common.ILogWrapper
import ru.otus.kotlin.brown.common.helpers.fail
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.helpers.asNotificationError
import ru.otus.kotlin.brown.biz.ProcessingWorkflow.Companion.stubBusinessChain

class NotificationProcessor : ProcessingWorkflow {
    suspend fun exec(ctx: NotificationContext) = stubBusinessChain.exec(ctx)

    suspend inline fun <reified T: IResponse> process(
        logger: ILogWrapper,
        crossinline fromTransport: suspend (NotificationContext) -> Unit,
        crossinline sendResponse: suspend (NotificationContext) -> Unit
    ) {

        val ctx = NotificationContext(timeStart = Clock.System.now())

        try {
            fromTransport(ctx)
            val logId = ctx.command.getRequestType()

            logger.doWithLogging(id = logId) {
                logger.info(
                    msg = "${ctx.command} request is got",
                    data = ctx.toLog("${logId}-got")
                )
                exec(ctx)
                logger.info(
                    msg = "${ctx.command} request is handled",
                    data = ctx.toLog("${logId}-handled")
                )
                sendResponse(ctx)
            }
        } catch (e: Throwable) {

            val logId = T::class.java.simpleName

            logger.doWithLogging(id = "${logId}-failure") {
                logger.error(msg = "${ctx.command} handling failed")

                ctx.fail(e.asNotificationError())
                exec(ctx)
                sendResponse(ctx)
            }
        }
    }
}
