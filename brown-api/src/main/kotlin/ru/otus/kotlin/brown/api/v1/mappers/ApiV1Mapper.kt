package ru.otus.kotlin.brown.api.v1.mappers

import ru.otus.kotlin.brown.api.v1.models.IRequest
import ru.otus.kotlin.brown.api.v1.models.IResponse
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.DeserializationFeature

val apiV1Mapper = JsonMapper.builder().run {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T = apiV1Mapper.readValue(json, IRequest::class.java) as T

fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T> apiV1ResponseDeserialize(json: String): T = apiV1Mapper.readValue(json, IResponse::class.java) as T
