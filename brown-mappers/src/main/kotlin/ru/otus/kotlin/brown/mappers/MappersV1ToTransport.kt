package ru.otus.kotlin.brown.mappers

import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.mappers.exceptions.UnknownRequest
import ru.otus.kotlin.brown.common.models.NotificationType as Type
import ru.otus.kotlin.brown.common.models.NotificationVisibility as Visibility

fun NotificationContext.toTransport(): IResponse {
    val isMultipleOutput = command === NotificationCommand.SEARCH

    val requestId = this.requestId.asString().takeIf { it.isNotBlank() }
    val result = if (this.state == NotificationState.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR
    val errors = this.errors.toTransportErrors()
    val notification = this.notificationResponse.toTransportNotification().takeIf { !isMultipleOutput }
    val notifications = this.notificationFilterResponse.toTransportNotification().takeIf { isMultipleOutput }

    return when (command) {
        NotificationCommand.CREATE -> NotificationCreateResponse(null, requestId, result,  errors, notification)
        NotificationCommand.READ -> NotificationReadResponse(null, requestId, result,  errors, notification)
        NotificationCommand.UPDATE -> NotificationUpdateResponse(null, requestId, result,  errors, notification)
        NotificationCommand.CANCEL -> NotificationCancelResponse(null, requestId, result,  errors, notification)
        NotificationCommand.SEARCH -> NotificationSearchResponse(null, requestId, result,  errors, notifications)
        else -> throw UnknownRequest()
    }
}

private fun List<Notification>.toTransportNotification(): List<NotificationResponseObject>? = this
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

private fun Visibility.toTransportNotification(): NotificationVisibility = when (this) {
    Visibility.PUBLIC -> NotificationVisibility.PUBLIC
    Visibility.PRIVATE -> NotificationVisibility.PRIVATE
}

private fun Type.toTransportNotification(): NotificationType = when (this) {
    Type.COMMON -> NotificationType.COMMON
    Type.WARNING -> NotificationType.WARNING
    Type.ALERT -> NotificationType.ALERT
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
