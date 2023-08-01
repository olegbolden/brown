package ru.otus.kotlin.brown.repo.stubs

import ru.otus.kotlin.brown.stubs.*
import ru.otus.kotlin.brown.common.repo.*
import ru.otus.kotlin.brown.common.models.*

class NotificationStubRepo() : INotificationRepository {
    override suspend fun createNotification(rq: DbNotificationRequest): DbNotificationResponse {
        return DbNotificationResponse(
            data = NotificationStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
        return DbNotificationResponse(
            data = NotificationStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateNotification(rq: DbNotificationRequest): DbNotificationResponse {
        return DbNotificationResponse(
            data = NotificationStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun cancelNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
        return DbNotificationResponse(
            data = NotificationStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchNotification(rq: DbNotificationFilterRequest): DbNotificationsResponse {
        return DbNotificationsResponse(
            data = NotificationStub.prepareSearchList(filterString = "", NotificationType.COMMON),
            isSuccess = true,
        )
    }
}
