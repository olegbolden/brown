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

package ru.otus.kotlin.brown.api.v1.models

import ru.otus.kotlin.brown.api.v1.models.NotificationType
import ru.otus.kotlin.brown.api.v1.models.NotificationVisibility

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param title Notification Title
 * @param description Notification Description
 * @param notificationType 
 * @param visibility 
 */


data class NotificationCreateObject (

    /* Notification Title */
    @field:JsonProperty("title")
    val title: kotlin.String? = null,

    /* Notification Description */
    @field:JsonProperty("description")
    val description: kotlin.String? = null,

    @field:JsonProperty("notificationType")
    val notificationType: NotificationType? = null,

    @field:JsonProperty("visibility")
    val visibility: NotificationVisibility? = null

)

