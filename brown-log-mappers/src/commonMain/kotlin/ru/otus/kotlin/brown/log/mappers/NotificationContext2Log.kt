package ru.otus.kotlin.brown.log.mappers

import kotlinx.datetime.Clock
import ru.otus.kotlin.brown.api.log.models.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext

fun NotificationContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "brown",
    notification = toNotificationLog(),
    errors = errors.map { it.toLog() },
)

fun NotificationContext.toNotificationLog():NotificationLogModel? {
    val notificationNone = Notification()
    return NotificationLogModel(
        requestId = requestId.takeIf { it != NotificationRequestId.NONE }?.asString(),
        requestNotification = requestNotification.takeIf { it != notificationNone }?.toLog(),
        responseNotification = responseNotification.takeIf { it != notificationNone }?.toLog(),
        responseNotificationList = responseNotificationList.takeIf { it.isNotEmpty() }?.filter { it != notificationNone }?.map { it.toLog() },
        requestFilter = requestNotificationFilter.takeIf { it != NotificationFilter() }?.toLog(),
    ).takeIf { it != NotificationLogModel() }
}

private fun NotificationFilter.toLog() = NotificationFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != NotificationUserId.NONE }?.asString(),
    type = type?.name,
)

fun NotificationError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

// ToDo Выяснить, чем отличаются id и notificationId
fun Notification.toLog() = NotificationLog(
    id = id.takeIf { it != NotificationId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    type = type.name,
    visibility = visibility.name,
    ownerId = ownerId.takeIf { it != NotificationUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
