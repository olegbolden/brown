package inMemotyRepoTests

import org.junit.Test

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.serialization.jackson.*

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import ru.otus.kotlin.brown.api.v1.models.*
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.DeserializationFeature

const val COMMON_REQUEST_ID = "fdf34391-4b9e-41d3-9295-38b723488245"

class V1NotificationInmemoryApiTest {
    private val createNotification = NotificationCreateObject(
        title = "Отключение электричества",
        description = "Планируется отключение электричества с 5 по 15 августа",
        type = NotificationType.COMMON,
        visibility = NotificationVisibility.PUBLIC,
    )

    private val requestCreateObj = NotificationCreateRequest(
        requestId = COMMON_REQUEST_ID,
        requestType = "create",
        notification = createNotification,
        debug = NotificationDebug(
            mode = NotificationRequestDebugMode.TEST,
        )
    )

    /**
     * At the beginning of each test we create object on the fly to work with it further as if it was stored in our DB for ages.
     */
    private suspend fun createObject(client: HttpClient): NotificationCreateResponse {
        println(requestCreateObj)
        val responseCreate = client.post("/v1/notification/create") {
            contentType(ContentType.Application.Json)
            setBody(requestCreateObj)
        }
        assertEquals(200, responseCreate.status.value)
        return responseCreate.body<NotificationCreateResponse>()
    }

    @Test
    fun create() = testApplication {
        val responseObj = createObject(client())
        assertEquals(createNotification.title, responseObj.notification?.title)
        assertEquals(createNotification.description, responseObj.notification?.description)
        assertEquals(createNotification.type, responseObj.notification?.type)
        assertEquals(createNotification.visibility, responseObj.notification?.visibility)
    }

    @Test
    fun read() = testApplication {
        val client = client()
        val id = createObject(client).notification?.id
        val response = client.post("/v1/notification/read") {
            val requestObj = NotificationReadRequest(
                requestId = COMMON_REQUEST_ID,
                requestType = "read",
                notification = NotificationReadObject(id),
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(id, responseObj.notification?.id)
    }

    @Test
    fun update() = testApplication {
        val client = client()

        val created = createObject(client)

        val notificationUpdate = NotificationUpdateObject(
            id = created.notification?.id,
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            type = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = created.notification?.lock,
        )

        val response = client.post("/v1/notification/update") {
            val requestObj = NotificationUpdateRequest(
                requestId = COMMON_REQUEST_ID,
                requestType = "update",
                notification = notificationUpdate,
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(notificationUpdate.id, responseObj.notification?.id)
        assertEquals(notificationUpdate.title, responseObj.notification?.title)
        assertEquals(notificationUpdate.description, responseObj.notification?.description)
        assertEquals(notificationUpdate.type, responseObj.notification?.type)
        assertEquals(notificationUpdate.visibility, responseObj.notification?.visibility)
    }

    @Test
    fun cancel() = testApplication {
        val client = client()
        val created = createObject(client)

        val response = client.post("/v1/notification/cancel") {
            val requestObj = NotificationCancelRequest(
                requestId = COMMON_REQUEST_ID,
                requestType = "cancel",
                notification = NotificationCancelObject(
                    id = created.notification?.id,
                    lock = created.notification?.lock
                ),
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationCancelResponse>()
        assertEquals(200, response.status.value)
        assertEquals(created.notification?.id, responseObj.notification?.id)
    }

    @Test
    fun search() = testApplication {
        val client = client()
        val initObject = createObject(client)
        val response = client.post("/v1/notification/search") {
            val requestObj = NotificationSearchRequest(
                requestId = COMMON_REQUEST_ID,
                requestType = "search",
                notificationFilter = NotificationSearchFilter(),
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.notifications?.size)
        assertEquals(initObject.notification?.id, responseObj.notifications?.first()?.id)
    }

    private fun ApplicationTestBuilder.client() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}
