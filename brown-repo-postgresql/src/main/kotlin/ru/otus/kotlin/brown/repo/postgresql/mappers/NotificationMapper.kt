package ru.otus.kotlin.brown.repo.postgresql.mappers

import org.jetbrains.exposed.sql.ResultRow
import ru.otus.kotlin.brown.common.models.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import ru.otus.kotlin.brown.repo.postgresql.entity.NotificationTable

fun ResultRow.toNotification(): Notification = Notification(
    id = NotificationId(this[NotificationTable.id].toString()),
    title = this[NotificationTable.title],
    description = this[NotificationTable.description],
    ownerId = NotificationUserId(this[NotificationTable.owner].toString()),
    visibility = this[NotificationTable.visibility],
    notificationType = this[NotificationTable.type],
    lock = NotificationLock(this[NotificationTable.lock])
)

fun Notification.toInsertStatement(statement: InsertStatement<Number>): InsertStatement<Number> = statement.also {
    it[NotificationTable.title] = this.title
    it[NotificationTable.description] = this.description
    it[NotificationTable.owner] = this.ownerId.asString()
    it[NotificationTable.visibility] = this.visibility
    it[NotificationTable.type] = this.notificationType
    it[NotificationTable.lock] = this.lock.asString()
}

fun Notification.toUpdateStatement(statement: UpdateStatement): UpdateStatement = statement.also {
    it[NotificationTable.title] = this.title
    it[NotificationTable.description] = this.description
    it[NotificationTable.owner] = this.ownerId.asString()
    it[NotificationTable.visibility] = this.visibility
    it[NotificationTable.type] = this.notificationType
    it[NotificationTable.lock] = this.lock.asString()
}