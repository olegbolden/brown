package ru.otus.kotlin.brown.biz

import kotlinx.datetime.Clock
import ru.otus.kotlin.brown.api.v1.models.IResponse
import ru.otus.kotlin.brown.log.common.ILogWrapper
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.helpers.asNotificationError
import ru.otus.kotlin.brown.common.helpers.fail
import ru.otus.kotlin.brown.common.models.NotificationCommand

suspend inline fun <reified T : IResponse> NotificationProcessor.process(
    logger: ILogWrapper,
    logId: String = "",
    crossinline fromTransport: suspend (NotificationContext) -> Unit,
    crossinline sendResponse: suspend (NotificationContext) -> Unit,
    crossinline toLog: NotificationContext.(logId: String) -> Any
) {

    val ctx = NotificationContext(timeStart = Clock.System.now())

    try {
        fromTransport(ctx)

        val outputLogId = createLogId<T>(ctx, logId)

        logger.doWithLogging(id = outputLogId) {
            logger.info(
                msg = "$outputLogId request is got",
                data = ctx.toLog("${outputLogId}-got")
            )
            exec(ctx)
            logger.info(
                msg = "$outputLogId request is handled",
                data = ctx.toLog("${outputLogId}-handled")
            )
            sendResponse(ctx)
        }
    } catch (e: Throwable) {
        val outputLogId = createLogId<T>(ctx, logId)

        logger.doWithLogging(id = "${outputLogId}-failure") {
            logger.error(msg = "$outputLogId handling failed")

            ctx.fail(e.asNotificationError())
            exec(ctx)
            sendResponse(ctx)
        }
    }
}

inline fun <reified T : IResponse> NotificationProcessor.createLogId(ctx: NotificationContext, logId: String): String {
    val internalLogId = ctx.command.takeIf { it != NotificationCommand.NONE }?.getResponseType()
    return logId.ifEmpty { internalLogId ?: T::class.java.simpleName }
}
