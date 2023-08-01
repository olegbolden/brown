package ru.otus.kotlin.brown.repo.inmemory.entity

import ru.otus.kotlin.brown.common.models.*

data class NotificationEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val status: String? = null,
    val type: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: Notification) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        status = model.status.name,
        type = model.type.name,
        visibility = model.visibility.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = Notification(
        id = id?.let { NotificationId(it) } ?: NotificationId.NONE,
        title = title ?: "",
        description = description ?: "",
        ownerId = ownerId?.let { NotificationUserId(it) } ?: NotificationUserId.NONE,
        status = status?.let { NotificationStatus.valueOf(it) } ?: NotificationStatus.OPEN,
        type = type?.let { NotificationType.valueOf(it) } ?: NotificationType.COMMON,
        visibility = visibility?.let { NotificationVisibility.valueOf(it) } ?: NotificationVisibility.PRIVATE,
        lock = lock?.let { NotificationLock(it) } ?: NotificationLock.NONE,
    )
}
