package ru.otus.kotlin.brown.mappers

import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationFilter
import ru.otus.kotlin.brown.common.models.NotificationType as Type
import ru.otus.kotlin.brown.common.models.NotificationVisibility as Visibility
import ru.otus.kotlin.brown.common.stubs.NotificationStubType
import ru.otus.kotlin.brown.mappers.exceptions.NullNotAllowed
import ru.otus.kotlin.brown.mappers.exceptions.UnknownNotificationCommand
import ru.otus.kotlin.brown.mappers.exceptions.ValueOutOfRange

fun NotificationContext.fromTransport(request: IRequest) {
    command = request.requestType.getNotificationCommand()
    requestId = request.requestId?.let { NotificationRequestId(it) } ?: NotificationRequestId.NONE
    workMode = when (request.debug?.mode) {
        NotificationRequestDebugMode.PROD -> NotificationWorkMode.PROD
        NotificationRequestDebugMode.TEST -> NotificationWorkMode.TEST
        NotificationRequestDebugMode.STUB -> NotificationWorkMode.STUB
        null -> NotificationWorkMode.PROD
    }
    stubCase = when (request.debug?.stub) {
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
    notificationRequest = when (request) {
        is NotificationCreateRequest -> request.notification?.toInternal() ?: Notification()
        is NotificationUpdateRequest -> request.notification?.toInternal() ?: Notification()
        is NotificationReadRequest -> Notification(id = request.notification?.id.toNotificationId())
        is NotificationCancelRequest -> Notification(id = request.notification?.id.toNotificationId())
        else -> Notification();
    }
    notificationFilterRequest = when (request) {
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

private fun NotificationCreateObject.toInternal(): Notification = Notification(
    title = this.title ?: "",
    description = this.description ?: "",
    notificationType = this.notificationType.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun NotificationUpdateObject.toInternal(): Notification = Notification(
    id = this.id.toNotificationId(),
    title = this.title ?: "",
    description = this.description ?: "",
    notificationType = this.notificationType.fromTransport(),
    visibility = this.visibility.fromTransport(),
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
