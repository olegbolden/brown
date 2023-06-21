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

import ru.otus.kotlin.brown.api.v1.models.IRequest
import ru.otus.kotlin.brown.api.v1.models.NotificationDebug
import ru.otus.kotlin.brown.api.v1.models.NotificationUpdateObject

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param requestType Discriminator string
 * @param requestId Request identifier for logging/debugging purposes (generated at client side)
 * @param debug 
 * @param notification 
 */


data class NotificationUpdateRequest (

    /* Discriminator string */
    @field:JsonProperty("requestType", access = JsonProperty.Access.WRITE_ONLY)
    override val requestType: kotlin.String,

    /* Request identifier for logging/debugging purposes (generated at client side) */
    @field:JsonProperty("requestId")
    override val requestId: kotlin.String? = null,

    @field:JsonProperty("debug")
    override val debug: NotificationDebug? = null,

    @field:JsonProperty("notification")
    val notification: NotificationUpdateObject? = null

) : IRequest

