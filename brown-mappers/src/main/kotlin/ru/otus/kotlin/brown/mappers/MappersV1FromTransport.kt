package ru.otus.kotlin.brown.mappers

import ru.otus.kotlin.brown.api.v1.models.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.models.NotificationType as Type
import ru.otus.kotlin.brown.common.models.NotificationVisibility as Visibility
import ru.otus.kotlin.brown.common.stubs.NotificationStubs
import ru.otus.kotlin.brown.mappers.exceptions.UnknownRequestClass

fun NotificationContext.fromTransport(request: IRequest) = when (request) {
    is NotificationCreateRequest -> fromTransport(request)
    is NotificationReadRequest -> fromTransport(request)
    is NotificationUpdateRequest -> fromTransport(request)
    is NotificationCancelRequest -> fromTransport(request)
    is NotificationSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toNotificationId() = this?.let { NotificationId(it) } ?: NotificationId.NONE
private fun String?.toNotificationWithId() = Notification(id = this.toNotificationId())
private fun IRequest?.requestId() = this?.requestId?.let { NotificationRequestId(it) } ?: NotificationRequestId.NONE

private fun NotificationDebug?.transportToWorkMode(): NotificationWorkMode = when (this?.mode) {
    NotificationRequestDebugMode.PROD -> NotificationWorkMode.PROD
    NotificationRequestDebugMode.TEST -> NotificationWorkMode.TEST
    NotificationRequestDebugMode.STUB -> NotificationWorkMode.STUB
    null -> NotificationWorkMode.PROD
}

private fun NotificationDebug?.transportToStubCase(): NotificationStubs = when (this?.stub) {
    NotificationRequestDebugStubs.SUCCESS -> NotificationStubs.SUCCESS
    NotificationRequestDebugStubs.NOT_FOUND -> NotificationStubs.NOT_FOUND
    NotificationRequestDebugStubs.BAD_ID -> NotificationStubs.BAD_ID
    NotificationRequestDebugStubs.BAD_TITLE -> NotificationStubs.BAD_TITLE
    NotificationRequestDebugStubs.BAD_DESCRIPTION -> NotificationStubs.BAD_DESCRIPTION
    NotificationRequestDebugStubs.BAD_VISIBILITY -> NotificationStubs.BAD_VISIBILITY
    NotificationRequestDebugStubs.CANNOT_CANCEL -> NotificationStubs.CANNOT_CANCEL
    NotificationRequestDebugStubs.BAD_SEARCH_STRING -> NotificationStubs.BAD_SEARCH_STRING
    null -> NotificationStubs.NONE
}

fun NotificationContext.fromTransport(request: NotificationCreateRequest) {
    command = NotificationCommand.CREATE
    requestId = request.requestId()
    notificationRequest = request.notification?.toInternal() ?: Notification()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun NotificationContext.fromTransport(request: NotificationReadRequest) {
    command = NotificationCommand.READ
    requestId = request.requestId()
    notificationRequest = request.notification?.id.toNotificationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun NotificationContext.fromTransport(request: NotificationUpdateRequest) {
    command = NotificationCommand.UPDATE
    requestId = request.requestId()
    notificationRequest = request.notification?.toInternal() ?: Notification()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun NotificationContext.fromTransport(request: NotificationCancelRequest) {
    command = NotificationCommand.CANCEL
    requestId = request.requestId()
    notificationRequest = request.notification?.id.toNotificationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun NotificationContext.fromTransport(request: NotificationSearchRequest) {
    command = NotificationCommand.SEARCH
    requestId = request.requestId()
    notificationFilterRequest = request.notificationFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun NotificationSearchFilter?.toInternal(): NotificationFilter = NotificationFilter(
    searchString = this?.searchString ?: ""
)

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
    NotificationVisibility.PUBLIC -> Visibility.VISIBLE_PUBLIC
    null -> Visibility.NONE
}

private fun NotificationType?.fromTransport(): Type = when (this) {
    NotificationType.COMMON -> Type.COMMON
    NotificationType.WARNING -> Type.WARNING
    NotificationType.ALERT -> Type.ALERT
    null -> Type.NONE
}
