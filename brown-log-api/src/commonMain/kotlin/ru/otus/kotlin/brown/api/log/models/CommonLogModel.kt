/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package ru.otus.kotlin.brown.api.log.models

import ru.otus.kotlin.brown.api.log.models.ErrorLogModel
import ru.otus.kotlin.brown.api.log.models.NotificationLogModel

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/**
 * Common model for logging of all microservices
 *
 * @param messageTime 
 * @param logId 
 * @param source 
 * @param notification 
 * @param errors 
 */
@Serializable

data class CommonLogModel (

    @SerialName(value = "messageTime") val messageTime: kotlin.String? = null,

    @SerialName(value = "logId") val logId: kotlin.String? = null,

    @SerialName(value = "source") val source: kotlin.String? = null,

    @SerialName(value = "notification") val notification: NotificationLogModel? = null,

    @SerialName(value = "errors") val errors: kotlin.collections.List<ErrorLogModel>? = null

)

