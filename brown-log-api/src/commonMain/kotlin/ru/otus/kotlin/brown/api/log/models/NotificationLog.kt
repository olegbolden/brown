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


import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/**
 * 
 *
 * @param id 
 * @param title 
 * @param description 
 * @param notificationType 
 * @param visibility 
 * @param ownerId 
 * @param permissions 
 */
@Serializable

data class NotificationLog (

    @SerialName(value = "id") val id: kotlin.String? = null,

    @SerialName(value = "title") val title: kotlin.String? = null,

    @SerialName(value = "description") val description: kotlin.String? = null,

    @SerialName(value = "notificationType") val notificationType: kotlin.String? = null,

    @SerialName(value = "visibility") val visibility: kotlin.String? = null,

    @SerialName(value = "ownerId") val ownerId: kotlin.String? = null,

    @SerialName(value = "permissions") val permissions: kotlin.collections.Set<kotlin.String>? = null

)

