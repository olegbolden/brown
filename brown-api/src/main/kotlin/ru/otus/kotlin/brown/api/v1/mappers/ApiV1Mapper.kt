package ru.otus.kotlin.brown.api.v1.mappers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val apiV1mapper = jacksonObjectMapper()

inline fun <reified T: Any> apiV1Deserialize(json: String): T = apiV1mapper.readValue(json, T::class.java)

fun apiV1Serialize(clazz: Any): String = apiV1mapper.writeValueAsString(clazz)
