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

import ru.otus.kotlin.brown.api.v1.models.NotificationRequestDebugMode
import ru.otus.kotlin.brown.api.v1.models.NotificationRequestDebugStubs

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param mode 
 * @param stub 
 */


data class NotificationDebug (

    @field:JsonProperty("mode")
    val mode: NotificationRequestDebugMode? = null,

    @field:JsonProperty("stub")
    val stub: NotificationRequestDebugStubs? = null

)

