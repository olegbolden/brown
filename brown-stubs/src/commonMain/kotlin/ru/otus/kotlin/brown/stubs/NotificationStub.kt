package ru.otus.kotlin.brown.stubs

import ru.otus.kotlin.brown.common.models.Notification
import ru.otus.kotlin.brown.common.models.NotificationId
import ru.otus.kotlin.brown.common.models.NotificationType
import ru.otus.kotlin.brown.stubs.ThunderNotificationStub.NOTIFICATION_ALERT
import ru.otus.kotlin.brown.stubs.ThunderNotificationStub.NOTIFICATION_WARNING

object NotificationStub {
    fun get(): Notification = NOTIFICATION_ALERT.copy()

    fun prepareResult(block: Notification.() -> Unit): Notification = get().apply(block)

    fun prepareSearchList(filterString: String, type: NotificationType) = listOf(
        notificationAlert("alert-888-01", filterString, type),
        notificationAlert("alert-888-02", filterString, type),
        notificationAlert("alert-888-03", filterString, type),
        notificationAlert("alert-888-04", filterString, type),
        notificationAlert("alert-888-05", filterString, type),
        notificationAlert("alert-888-06", filterString, type),
    )

    private fun notificationAlert(id: String, filterString: String, type: NotificationType) =
        notification(NOTIFICATION_ALERT, id = id, filterString = filterString, type = type)

    private fun notificationWarning(id: String, filterString: String, type: NotificationType) =
        notification(NOTIFICATION_WARNING, id = id, filterString = filterString, type = type)

    private fun notification(base: Notification, id: String, filterString: String, type: NotificationType) = base.copy(
        id = NotificationId(id),
        title = "$filterString $id",
        description = "desc $filterString $id",
        type = type,
    )
}
