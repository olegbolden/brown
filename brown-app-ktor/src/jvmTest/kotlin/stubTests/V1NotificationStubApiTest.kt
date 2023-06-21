package stubTests

import io.ktor.http.*
import org.junit.Test
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.assertEquals
import io.ktor.serialization.jackson.*
import ru.otus.kotlin.brown.api.v1.models.*
import io.ktor.client.plugins.contentnegotiation.*
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.DeserializationFeature

/**
 * Notification objects are excluded from request objects since they do not processed in stub mode
 */
class V1AdStubApiTest {
    @Test
    fun create() = testApplication {
        val client = client()

        val response = client.post("/v1/notification/create") {
            val requestObj = NotificationCreateRequest(
                requestType = "create",
                requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.STUB,
                    stub = NotificationRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationCreateResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("888", responseObj.notification?.id)
    }

    @Test
    fun read() = testApplication {
        val client = client()

        val response = client.post("/v1/notification/read") {
            val requestObj = NotificationReadRequest(
                requestType = "read",
                requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.STUB,
                    stub = NotificationRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("888", responseObj.notification?.id)
    }

    @Test
    fun update() = testApplication {
        val client = client()

        val response = client.post("/v1/notification/update") {
            val requestObj = NotificationUpdateRequest(
                requestType = "update",
                requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.STUB,
                    stub = NotificationRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("888", responseObj.notification?.id)
    }

    @Test
    fun cancel() = testApplication {
        val client = client()

        val response = client.post("/v1/notification/cancel") {
            val requestObj = NotificationCancelRequest(
                requestType = "cancel",
                requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.STUB,
                    stub = NotificationRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationCancelResponse>()
        assertEquals(200, response.status.value)
        assertEquals("888", responseObj.notification?.id)
    }

    @Test
    fun search() = testApplication {
        val client = client()

        val response = client.post("/v1/notification/search") {
            val requestObj = NotificationSearchRequest(
                requestType = "search",
                requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.STUB,
                    stub = NotificationRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("alert-888-01", responseObj.notifications?.first()?.id)
    }

    private fun ApplicationTestBuilder.client() = createClient {
        install(ContentNegotiation) {
            jackson {
                writerWithDefaultPrettyPrinter()
                enable(SerializationFeature.INDENT_OUTPUT)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }
}
