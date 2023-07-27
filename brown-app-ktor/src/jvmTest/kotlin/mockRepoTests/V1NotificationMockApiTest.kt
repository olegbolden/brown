package mockRepoTests

import org.junit.Test
import io.ktor.http.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import io.ktor.serialization.jackson.*
import io.ktor.client.plugins.contentnegotiation.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.stubs.NotificationStub
import ru.otus.kotlin.brown.common.repo.DbNotificationResponse
import ru.otus.kotlin.brown.repo.tests.NotificationRepositoryMock
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.server.config.*
import ru.otus.kotlin.brown.app.ktor.moduleJvm
import ru.otus.kotlin.brown.app.ktor.settings.AppSettings
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.common.models.Notification
import ru.otus.kotlin.brown.common.models.NotificationLock
import ru.otus.kotlin.brown.common.repo.DbNotificationsResponse
import ru.otus.kotlin.brown.common.repo.INotificationRepository

class V1NotificationMockApiTest {
    private val stub = NotificationStub.get()
    private val userId = stub.ownerId
    private val notificationId = stub.id

    @Test
    fun create() = testApplication {
        initRepoTest(NotificationRepositoryMock(
            invokeCreateNotification = {
                DbNotificationResponse(
                    isSuccess = true,
                    data = it.notification.copy(id = notificationId),
                )
            }
        ))

        val client = myClient()

        val createNotification = NotificationCreateObject(
            title = "Ухудшение погоды",
            description = "Надвигается грозовой фронт",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
        )

        val response = client.post("/v1/notification/create") {
            val requestObj = NotificationCreateRequest(
                requestId = "12345",
                requestType = "create",
                notification = createNotification,
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(notificationId.asString(), responseObj.notification?.id)
        assertEquals(createNotification.title, responseObj.notification?.title)
        assertEquals(createNotification.description, responseObj.notification?.description)
        assertEquals(createNotification.notificationType, responseObj.notification?.notificationType)
        assertEquals(createNotification.visibility, responseObj.notification?.visibility)
    }

    @Test
    fun read() = testApplication {
        initRepoTest(NotificationRepositoryMock(
            invokeReadNotification = {
                DbNotificationResponse(
                    isSuccess = true,
                    data = Notification(
                        id = it.id,
                        ownerId = userId,
                    ),
                )
            }
        ))

        val client = myClient()

        val response = client.post("/v1/notification/read") {
            val requestObj = NotificationReadRequest(
                requestId = "12345",
                requestType = "read",
                notification = NotificationReadObject(notificationId.asString()),
                debug = NotificationDebug(
                    mode = NotificationRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<NotificationReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(notificationId.asString(), responseObj.notification?.id)
    }

    @Test
    fun update() = testApplication {
        initRepoTest(NotificationRepositoryMock(
            invokeReadNotification = {
                DbNotificationResponse(
                    isSuccess = true,
                    data = Notification(
                        id = it.id,
                        ownerId = userId,
                        lock = NotificationLock("123"),
                    ),
                )
            },
            invokeUpdateNotification = {
                DbNotificationResponse(
                    isSuccess = true,
                    data = it.notification.copy(ownerId = userId),
                )
            }
        ))

        val client = myClient()

        val notificationUpdate = NotificationUpdateObject(
            id = "888",
            title = "Ухудшение погоды",
            description = "Надвигается грозовой фронт",
            notificationType = NotificationType.COMMON,
            visibility = NotificationVisibility.PUBLIC,
            lock = "123",
        )

        val response = client.post("/v1/notification/update") {
            val requestObj = NotificationUpdateRequest(
                requestId = "12345",
                requestType = "update",
                notification = NotificationUpdateObject(
                    id = "888",
                    title = "Ухудшение погоды",
                    description = "Надвигается грозовой фронт",
                    notificationType = NotificationType.COMMON,
                    visibility = NotificationVisibility.PUBLIC,
                    lock = "123",
                ),
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
        assertEquals(notificationUpdate.notificationType, responseObj.notification?.notificationType)
        assertEquals(notificationUpdate.visibility, responseObj.notification?.visibility)
    }

    @Test
    fun cancel() = testApplication {
        initRepoTest(NotificationRepositoryMock(
            invokeReadNotification = {
                DbNotificationResponse(
                    isSuccess = true,
                    data = Notification(
                        id = it.id,
                        ownerId = userId,
                    ),
                )
            },
            invokeCancelNotification = {
                DbNotificationResponse(
                    isSuccess = true,
                    data = Notification(
                        id = it.id,
                        ownerId = userId,
                    ),
                )
            }
        ))

        val client = myClient()

        val deleteId = "666"

        val response = client.post("/v1/notification/cancel") {
            val requestObj = NotificationCancelRequest(
                requestId = "12345",
                requestType = "cancel",
                notification = NotificationCancelObject(
                    id = deleteId,
                    lock = "123",
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
        assertEquals(deleteId, responseObj.notification?.id)
    }

    @Test
    fun search() = testApplication {
        initRepoTest(NotificationRepositoryMock(
            invokeSearchNotification = {
                DbNotificationsResponse(
                    isSuccess = true,
                    data = listOf(
                        Notification(
                            title = it.titleFilter,
                            ownerId = it.ownerId,
                            notificationType = it.notificationType!!,
                        )
                    ),
                )
            }
        ))
        val client = myClient()

        val response = client.post("/v1/notification/search") {
            val requestObj = NotificationSearchRequest(
                requestId = "12345",
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
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    private fun ApplicationTestBuilder.initRepoTest(repo: INotificationRepository) {
        environment {
            config = MapApplicationConfig()
        }
        application {
            moduleJvm(AppSettings(corSettings = CorSettings(repoTest = repo)))
        }
    }
}
