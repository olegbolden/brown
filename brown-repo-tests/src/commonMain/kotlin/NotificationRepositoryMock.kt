package ru.otus.kotlin.brown.repo.tests

import ru.otus.kotlin.brown.common.repo.*

class NotificationRepositoryMock(
    private val invokeCreateNotification: (DbNotificationRequest) -> DbNotificationResponse = { DbNotificationResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadNotification: (DbNotificationIdRequest) -> DbNotificationResponse = { DbNotificationResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateNotification: (DbNotificationRequest) -> DbNotificationResponse = { DbNotificationResponse.MOCK_SUCCESS_EMPTY },
    private val invokeCancelNotification: (DbNotificationIdRequest) -> DbNotificationResponse = { DbNotificationResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchNotification: (DbNotificationFilterRequest) -> DbNotificationsResponse = { DbNotificationsResponse.MOCK_SUCCESS_EMPTY },
): INotificationRepository {
    override suspend fun createNotification(rq: DbNotificationRequest): DbNotificationResponse {
        return invokeCreateNotification(rq)
    }

    override suspend fun readNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
        return invokeReadNotification(rq)
    }

    override suspend fun updateNotification(rq: DbNotificationRequest): DbNotificationResponse {
        return invokeUpdateNotification(rq)
    }

    override suspend fun cancelNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
        return invokeCancelNotification(rq)
    }

    override suspend fun searchNotification(rq: DbNotificationFilterRequest): DbNotificationsResponse {
        return invokeSearchNotification(rq)
    }
}
