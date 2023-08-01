package ru.otus.kotlin.brown.repo.postgresql.mappers

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import ru.otus.kotlin.brown.common.models.Notification
import ru.otus.kotlin.brown.common.models.NotificationId
import ru.otus.kotlin.brown.common.models.NotificationLock
import ru.otus.kotlin.brown.common.models.NotificationUserId
import ru.otus.kotlin.brown.repo.postgresql.entity.NotificationTable

fun ResultRow.toNotification(): Notification = Notification(
    id = NotificationId(this[NotificationTable.id].toString()),
    title = this[NotificationTable.title],
    description = this[NotificationTable.description],
    ownerId = NotificationUserId(this[NotificationTable.owner].toString()),
    visibility = this[NotificationTable.visibility],
    status = this[NotificationTable.status],
    type = this[NotificationTable.type],
    lock = NotificationLock(this[NotificationTable.lock])
)

fun Notification.toInsertStatement(statement: InsertStatement<Number>): InsertStatement<Number> = statement.also {
    it[NotificationTable.title] = this.title
    it[NotificationTable.description] = this.description
    it[NotificationTable.owner] = this.ownerId.asString()
    it[NotificationTable.visibility] = this.visibility
    it[NotificationTable.status] = this.status
    it[NotificationTable.type] = this.type
    it[NotificationTable.lock] = uuid4().toString()
}

fun Notification.toUpdateStatement(statement: UpdateStatement): UpdateStatement = statement.also {
    it[NotificationTable.title] = this.title
    it[NotificationTable.description] = this.description
    it[NotificationTable.owner] = this.ownerId.asString()
    it[NotificationTable.visibility] = this.visibility
    it[NotificationTable.status] = this.status
    it[NotificationTable.type] = this.type
    it[NotificationTable.lock] = this.lock.asString()
}