import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.api.v1.mappers.apiV1Mapper
import ru.otus.kotlin.brown.api.v1.mappers.apiV1ResponseDeserialize
import ru.otus.kotlin.brown.api.v1.mappers.apiV1ResponseSerialize

class ResponseSerializationTest {
    private val response = NotificationCreateResponse(
        requestId = "22fc6e6f-2f46-46c2-b701-d159bda43acc",
        notification = NotificationResponseObject(
            title = "Important notification",
            description = "Don't forget to phone to your parents",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1ResponseSerialize(response)

        assertContains(json, Regex("\"title\":\\s*\"Important notification\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1ResponseSerialize(response)
        val obj = apiV1ResponseDeserialize(json) as NotificationCreateResponse
        assertEquals(response, obj)
    }
}
