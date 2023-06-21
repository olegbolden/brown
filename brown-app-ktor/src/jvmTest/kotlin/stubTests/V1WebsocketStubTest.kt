package ru.otus.otuskotlin.marketplace.app.stubs

import kotlin.test.*
import io.ktor.websocket.*
import io.ktor.server.testing.*
import io.ktor.client.plugins.websocket.*
import kotlinx.coroutines.withTimeoutOrNull
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.api.v1.mappers.*

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = NotificationCreateRequest(
            requestType = "create",
            requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
            debug = NotificationDebug(
                mode = NotificationRequestDebugMode.STUB,
                stub = NotificationRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("22fc6e6f-2f46-46c2-b701-d159bda43acc", it.requestId)
            assertEquals("create", it.responseType)
        }
    }

    @Test
    fun readStub() {
        val request = NotificationReadRequest(
            requestType = "read",
            requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
            debug = NotificationDebug(
                mode = NotificationRequestDebugMode.STUB,
                stub = NotificationRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("22fc6e6f-2f46-46c2-b701-d159bda43acc", it.requestId)
            assertEquals("read", it.responseType)
        }
    }

    @Test
    fun updateStub() {
        val request = NotificationUpdateRequest(
            requestType = "update",
            requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
            debug = NotificationDebug(
                mode = NotificationRequestDebugMode.STUB,
                stub = NotificationRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("22fc6e6f-2f46-46c2-b701-d159bda43acc", it.requestId)
            assertEquals("update", it.responseType)
        }
    }

    @Test
    fun cancelStub() {
        val request = NotificationCancelRequest(
            requestType = "cancel",
            requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
            debug = NotificationDebug(
                mode = NotificationRequestDebugMode.STUB,
                stub = NotificationRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("22fc6e6f-2f46-46c2-b701-d159bda43acc", it.requestId)
            assertEquals("cancel", it.responseType)
        }
    }

    @Test
    fun searchStub() {
        val request = NotificationSearchRequest(
            requestType = "search",
            requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
            debug = NotificationDebug(
                mode = NotificationRequestDebugMode.STUB,
                stub = NotificationRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("22fc6e6f-2f46-46c2-b701-d159bda43acc", it.requestId)
            assertEquals("search", it.responseType)
        }
    }

    private inline fun <reified T> testMethod(
        request: Any,
        crossinline assertBlock: (T) -> Unit
    ) {
        testApplication {
            val client = createClient {
                install(WebSockets)
            }

            client.webSocket("/ws/v1") {
                // Init connection
                withTimeoutOrNull(3000) {
                    val initResponse: T = apiV1Deserialize((incoming.receive() as Frame.Text).readText())
                    assertIs<T>(initResponse)
                }

                // Send request
                outgoing.send(Frame.Text(apiV1Serialize(request)))

                // Get and process response
                withTimeoutOrNull(3000) {
                    val response: T = apiV1Deserialize((incoming.receive() as Frame.Text).readText())
                    assertBlock(response)
                }
            }
        }
    }
}
