package ru.otus.kotlin.brown.common.repo

import ru.otus.kotlin.brown.common.models.NotificationError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<NotificationError>
}
