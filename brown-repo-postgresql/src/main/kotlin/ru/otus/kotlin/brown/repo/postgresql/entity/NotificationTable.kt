package ru.otus.kotlin.brown.repo.postgresql.entity

import org.jetbrains.exposed.dao.id.IdTable
import ru.otus.kotlin.brown.common.models.NotificationStatus
import ru.otus.kotlin.brown.common.models.NotificationType
import ru.otus.kotlin.brown.common.models.NotificationVisibility

object NotificationTable : IdTable<Int>("notification") {
    override val id = integer("id").entityId()
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val owner = varchar("owner", 128)
    val visibility = enumerationByName<NotificationVisibility>("visibility", 10)
    val status = enumerationByName<NotificationStatus>("status", 10)
    val type = enumerationByName<NotificationType>("type", 10)
    val lock = varchar("lock", 64)

    override val primaryKey = PrimaryKey(id)
}
