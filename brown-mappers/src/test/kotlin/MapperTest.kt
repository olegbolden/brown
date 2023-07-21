import org.junit.Test
import kotlin.test.assertEquals
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.stubs.NotificationStubType
import ru.otus.kotlin.brown.common.models.NotificationType as Type
import ru.otus.kotlin.brown.common.models.NotificationVisibility as Visibility
import ru.otus.kotlin.brown.mappers.fromTransport
import ru.otus.kotlin.brown.mappers.toTransport

class MapperTest {
    @Test
    fun fromTransport() {
        val req = NotificationCreateRequest(
            requestType="create",
            requestId = "1234",
            debug = NotificationDebug(
                mode = NotificationRequestDebugMode.STUB,
                stub = NotificationRequestDebugStubs.SUCCESS,
            ),
            notification = NotificationCreateObject(
                title = "title",
                description = "desc",
                notificationType = NotificationType.ALERT,
                visibility = NotificationVisibility.PUBLIC,
            ),
        )

        val context = NotificationContext()
        context.fromTransport(req)

        assertEquals(NotificationStubType.SUCCESS, context.stubType)
        assertEquals(NotificationWorkMode.STUB, context.workMode)
        assertEquals("title", context.requestNotification.title)
        assertEquals(Visibility.PUBLIC, context.requestNotification.visibility)
        assertEquals(Type.ALERT, context.requestNotification.notificationType)
    }

    @Test
    fun toTransport() {
        val context = NotificationContext(
            requestId = NotificationRequestId("1234"),
            command = NotificationCommand.CREATE,
            responseNotification = Notification(
                title = "title",
                description = "desc",
                notificationType = Type.ALERT,
                visibility = Visibility.PUBLIC,
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

        val req = context.toTransport() as NotificationCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("title", req.notification?.title)
        assertEquals("desc", req.notification?.description)
        assertEquals(NotificationVisibility.PUBLIC, req.notification?.visibility)
        assertEquals(NotificationType.ALERT, req.notification?.notificationType)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
