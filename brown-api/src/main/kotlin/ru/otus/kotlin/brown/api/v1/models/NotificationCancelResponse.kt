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

import ru.otus.kotlin.brown.api.v1.models.Error
import ru.otus.kotlin.brown.api.v1.models.IResponse
import ru.otus.kotlin.brown.api.v1.models.NotificationResponseObject
import ru.otus.kotlin.brown.api.v1.models.ResponseResult

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param responseType Discriminator string
 * @param requestId Request identifier for logging/debugging purposes
 * @param result 
 * @param errors 
 * @param notification 
 */


data class NotificationCancelResponse (

    /* Discriminator string */
    @field:JsonProperty("responseType", access = JsonProperty.Access.WRITE_ONLY)
    override val responseType: kotlin.String,

    /* Request identifier for logging/debugging purposes */
    @field:JsonProperty("requestId")
    override val requestId: kotlin.String? = null,

    @field:JsonProperty("result")
    override val result: ResponseResult? = null,

    @field:JsonProperty("errors")
    override val errors: kotlin.collections.List<Error>? = null,

    @field:JsonProperty("notification")
    val notification: NotificationResponseObject? = null

) : IResponse

