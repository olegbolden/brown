package ru.otus.kotlin.brown.api.v1.models

/**
 * DTO to initialize relevant  Notification***Response class
 */
data class NotificationGeneralResponse (
    override val responseType: String? = null,
    override val requestId: String? = null,
    override val result: ResponseResult? = null,
    override val errors: List<Error>? = null,
    val notification: NotificationResponseObject? = null,
    val notifications: List<NotificationResponseObject>? = null
) : IResponse
