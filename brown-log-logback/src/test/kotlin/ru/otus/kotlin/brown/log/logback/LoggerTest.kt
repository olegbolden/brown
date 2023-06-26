package ru.otus.kotlin.brown.log.logback

import kotlin.test.Test
import java.io.PrintStream
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream

class LoggerTest {
    private val logId = "test-logger"

    @Test
    fun `logger init`() {
        val output = invokeLogger {
            println("Some action")
        }

        assertTrue(Regex("Started .* $logId.*").containsMatchIn(output.toString()))
        assertTrue(output.toString().contains("Some action"))
        assertTrue(Regex("Finished .* $logId.*").containsMatchIn(output.toString()))
    }

    @Test
    fun `logger fails`() {
        val output = invokeLogger {
            throw RuntimeException("Some action")
        }

        assertTrue(Regex("Started .* $logId.*").containsMatchIn(output.toString()))
        assertTrue(Regex("Failed .* $logId.*").containsMatchIn(output.toString()))
    }

    private fun invokeLogger(block: suspend () -> Unit): ByteArrayOutputStream {
        val outputStreamCaptor = outputStreamCaptor()

        try {
            runBlocking {
                val logger = logbackLogger(this::class)
                logger.doWithLogging(logId, block = block)
            }
        } catch (ignore: RuntimeException) {
        }

        return outputStreamCaptor
    }

    private fun outputStreamCaptor(): ByteArrayOutputStream {
        return ByteArrayOutputStream().apply {
            System.setOut(PrintStream(this))
        }
    }
}
