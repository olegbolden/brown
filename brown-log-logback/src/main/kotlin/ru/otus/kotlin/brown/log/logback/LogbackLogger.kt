package ru.otus.kotlin.brown.log.logback

import kotlin.reflect.KClass
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Logger
import ru.otus.kotlin.brown.log.common.ILogWrapper

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun logbackLogger(logger: Logger): ILogWrapper = LogbackWrapper(
    logger = logger,
    loggerId = logger.name,
)

fun logbackLogger(clazz: KClass<*>): ILogWrapper = logbackLogger(LoggerFactory.getLogger(clazz.java) as Logger)

fun logbackLogger(loggerId: String): ILogWrapper = logbackLogger(LoggerFactory.getLogger(loggerId) as Logger)
