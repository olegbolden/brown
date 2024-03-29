package ru.otus.kotlin.brown.mappers

import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.stubs.NotificationStubType
import ru.otus.kotlin.brown.mappers.exceptions.NullNotAllowed
import ru.otus.kotlin.brown.mappers.exceptions.UnknownNotificationCommand
import ru.otus.kotlin.brown.mappers.exceptions.ValueOutOfRange
import ru.otus.kotlin.brown.common.models.NotificationStatus as Status
import ru.otus.kotlin.brown.common.models.NotificationType as Type
import ru.otus.kotlin.brown.common.models.NotificationVisibility as Visibility

fun NotificationContext.fromTransport(request: IRequest) {
    command = request.requestType.getNotificationCommand()
    requestId = request.requestId?.let { NotificationRequestId(it) } ?: NotificationRequestId.NONE
    workMode = when (request.debug?.mode) {
        NotificationRequestDebugMode.PROD -> NotificationWorkMode.PROD
        NotificationRequestDebugMode.TEST -> NotificationWorkMode.TEST
        NotificationRequestDebugMode.STUB -> NotificationWorkMode.STUB
        null -> NotificationWorkMode.PROD
    }
    stubType = when (request.debug?.stub) {
        NotificationRequestDebugStubs.SUCCESS -> NotificationStubType.SUCCESS
        NotificationRequestDebugStubs.NOT_FOUND -> NotificationStubType.NOT_FOUND
        NotificationRequestDebugStubs.BAD_ID -> NotificationStubType.BAD_ID
        NotificationRequestDebugStubs.BAD_TITLE -> NotificationStubType.BAD_TITLE
        NotificationRequestDebugStubs.BAD_DESCRIPTION -> NotificationStubType.BAD_DESCRIPTION
        NotificationRequestDebugStubs.BAD_VISIBILITY -> NotificationStubType.BAD_VISIBILITY
        NotificationRequestDebugStubs.CANNOT_CANCEL -> NotificationStubType.CANNOT_CANCEL
        NotificationRequestDebugStubs.BAD_SEARCH_STRING -> NotificationStubType.BAD_SEARCH_STRING
        null -> NotificationStubType.NONE
    }
    requestNotification = when (request) {
        is NotificationCreateRequest -> request.notification?.toInternal() ?: Notification()
        is NotificationUpdateRequest -> request.notification?.toInternal() ?: Notification()
        is NotificationReadRequest -> request.notification.toInternal()
        is NotificationCancelRequest -> request.notification.toInternal()
        else -> Notification()
    }
    requestNotificationFilter = when (request) {
        is NotificationSearchRequest -> NotificationFilter(searchString = request.notificationFilter?.searchString ?: "")
        else -> NotificationFilter()
    }
}

fun String?.getNotificationCommand() = when (this) {
    "create" -> NotificationCommand.CREATE
    "read" -> NotificationCommand.READ
    "update" -> NotificationCommand.UPDATE
    "cancel" -> NotificationCommand.CANCEL
    "search" -> NotificationCommand.SEARCH
    null -> NotificationCommand.NONE
    else -> throw UnknownNotificationCommand()
}

private fun String?.toNotificationId() = this?.let { NotificationId(it) } ?: NotificationId.NONE
private fun String?.toNotificationLock() = this?.let { NotificationLock(it) } ?: NotificationLock.NONE

private fun NotificationCreateObject.toInternal(): Notification = Notification(
    title = this.title ?: "",
    description = this.description ?: "",
    type = this.type.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun NotificationReadObject?.toInternal(): Notification = if (this != null) {
    Notification(id = id.toNotificationId())
} else {
    Notification.NONE
}

private fun NotificationCancelObject?.toInternal(): Notification = if (this != null) {
    Notification(
        id = id.toNotificationId(),
        lock = lock.toNotificationLock(),
    )
} else {
    Notification.NONE
}

private fun NotificationUpdateObject.toInternal(): Notification = Notification(
    id = this.id.toNotificationId(),
    title = this.title ?: "",
    description = this.description ?: "",
    status = this.status.fromTransport(),
    type = this.type.fromTransport(),
    visibility = this.visibility.fromTransport(),
    lock = lock.toNotificationLock(),
)

private fun NotificationVisibility?.fromTransport(): Visibility = when (this) {
    NotificationVisibility.PUBLIC -> Visibility.PUBLIC
    NotificationVisibility.PRIVATE -> Visibility.PRIVATE
    null -> throw NullNotAllowed()
    else -> throw ValueOutOfRange(this.javaClass)
}

private fun NotificationType?.fromTransport(): Type = when (this) {
    NotificationType.COMMON -> Type.COMMON
    NotificationType.WARNING -> Type.WARNING
    NotificationType.ALERT -> Type.ALERT
    null -> throw NullNotAllowed()
    else -> throw ValueOutOfRange(this.javaClass)
}

private fun NotificationStatus?.fromTransport(): Status = when (this) {
    NotificationStatus.OPEN -> Status.OPEN
    NotificationStatus.CLOSED -> Status.CLOSED
    null -> throw NullNotAllowed()
    else -> throw ValueOutOfRange(this.javaClass)
}
