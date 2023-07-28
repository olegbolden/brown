import kotlin.test.Test
import kotlin.test.assertEquals
import ru.otus.kotlin.brown.log.mappers.toLog
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext

class MapperTest {

    @Test
    fun fromContext() {
        val context = NotificationContext(
            requestId = NotificationRequestId("052db4ae-c06e-454f-8d36-8619c3d2c433"),
            command = NotificationCommand.CREATE,
            responseNotification = Notification(
                title = "title",
                description = "desc",
                type = NotificationType.ALERT,
                visibility = NotificationVisibility.PUBLIC,
            ),
            errors = mutableListOf(
                NotificationError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = NotificationState.RUNNING,
        )

        val log = context.toLog("test-id")

        assertEquals("test-id", log.logId)
        assertEquals("brown", log.source)
        assertEquals("052db4ae-c06e-454f-8d36-8619c3d2c433", log.notification?.requestId)
        assertEquals("PUBLIC", log.notification?.responseNotification?.visibility)

        val error = log.errors?.firstOrNull()

        assertEquals("wrong title", error?.message)
        assertEquals("ERROR", error?.level)
    }
}
