import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.api.v1.mappers.*

class RequestSerializationTest {
    private val request = NotificationCreateRequest(
        requestType = "create",
        requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
        debug = NotificationDebug(
            mode = NotificationRequestDebugMode.STUB,
            stub = NotificationRequestDebugStubs.BAD_TITLE
        ),
        notification = NotificationCreateObject(
            title = "Important notification",
            description = "Don't forget to phone to your parents",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Serialize(request)

        assertContains(json, Regex("\"title\":\\s*\"Important notification\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Serialize(request)
        val obj = apiV1Deserialize<NotificationCreateRequest>(json)

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {
                "requestType": "create",            
                "requestId": "22fc6e6f-2f46-46c2-b701-d159bda43acc"
            }
        """.trimIndent()

        val obj = apiV1Deserialize<NotificationCreateRequest>(jsonString)

        assertEquals("22fc6e6f-2f46-46c2-b701-d159bda43acc", obj.requestId)
    }
}
