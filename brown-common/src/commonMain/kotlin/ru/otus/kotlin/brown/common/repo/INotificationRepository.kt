package ru.otus.kotlin.brown.common.repo

interface INotificationRepository {
    suspend fun createNotification(rq: DbNotificationRequest): DbNotificationResponse
    suspend fun readNotification(rq: DbNotificationIdRequest): DbNotificationResponse
    suspend fun updateNotification(rq: DbNotificationRequest): DbNotificationResponse
    suspend fun cancelNotification(rq: DbNotificationIdRequest): DbNotificationResponse
    suspend fun searchNotification(rq: DbNotificationFilterRequest): DbNotificationsResponse
    companion object {
        val NONE = object : INotificationRepository {
            override suspend fun createNotification(rq: DbNotificationRequest): DbNotificationResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateNotification(rq: DbNotificationRequest): DbNotificationResponse {
                TODO("Not yet implemented")
            }

            override suspend fun cancelNotification(rq: DbNotificationIdRequest): DbNotificationResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchNotification(rq: DbNotificationFilterRequest): DbNotificationsResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
