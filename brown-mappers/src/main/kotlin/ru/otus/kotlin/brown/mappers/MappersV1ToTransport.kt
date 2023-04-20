package ru.otus.kotlin.brown.mappers

import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationVisibility as Visibility
import ru.otus.kotlin.brown.common.models.NotificationType as Type
import ru.otus.kotlin.brown.mappers.exceptions.UnknownNotificationCommand

fun NotificationContext.toTransportNotification(): IResponse = when (val cmd = command) {
    NotificationCommand.CREATE -> toTransportCreate()
    NotificationCommand.READ -> toTransportRead()
    NotificationCommand.UPDATE -> toTransportUpdate()
    NotificationCommand.CANCEL-> toTransportCancel()
    NotificationCommand.SEARCH -> toTransportSearch()
    NotificationCommand.NONE -> throw UnknownNotificationCommand(cmd)
}

fun NotificationContext.toTransportCreate() = NotificationCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == NotificationState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    notification = notificationResponse.toTransportNotification()
)

fun NotificationContext.toTransportRead() = NotificationReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == NotificationState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    notification = notificationResponse.toTransportNotification()
)

fun NotificationContext.toTransportUpdate() = NotificationUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == NotificationState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    notification = notificationResponse.toTransportNotification()
)

fun NotificationContext.toTransportCancel() = NotificationCancelResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == NotificationState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    notification = notificationResponse.toTransportNotification()
)

fun NotificationContext.toTransportSearch() = NotificationSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == NotificationState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    notifications = notificationsResponse.toTransportNotification()
)

fun List<Notification>.toTransportNotification(): List<NotificationResponseObject>? = this
    .map { it.toTransportNotification() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun NotificationContext.toTransportInit() = NotificationInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

private fun Notification.toTransportNotification(): NotificationResponseObject = NotificationResponseObject(
    id = id.takeIf { it != NotificationId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != NotificationUserId.NONE }?.asString(),
    notificationType = notificationType.toTransportNotification(),
    visibility = visibility.toTransportNotification(),
    permissions = permissionsClient.toTransportNotification(),
)

private fun Set<NotificationPermissionClient>.toTransportNotification(): Set<NotificationPermissions>? = this
    .map { it.toTransportNotification() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun NotificationPermissionClient.toTransportNotification() = when (this) {
    NotificationPermissionClient.READ -> NotificationPermissions.READ
    NotificationPermissionClient.UPDATE -> NotificationPermissions.UPDATE
    NotificationPermissionClient.MAKE_VISIBLE_PUBLIC -> NotificationPermissions.MAKE_VISIBLE_PUBLIC
    NotificationPermissionClient.CANCEL -> NotificationPermissions.CANCEL
}

private fun Visibility.toTransportNotification(): NotificationVisibility? = when (this) {
    Visibility.VISIBLE_PUBLIC -> NotificationVisibility.PUBLIC
    Visibility.NONE -> null
}

private fun Type.toTransportNotification(): NotificationType? = when (this) {
    Type.COMMON -> NotificationType.COMMON
    Type.WARNING -> NotificationType.WARNING
    Type.ALERT -> NotificationType.ALERT
    Type.NONE -> null
}

private fun List<NotificationError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportNotification() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun NotificationError.toTransportNotification() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)