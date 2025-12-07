package com.example.jobflick.core.network

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable
data class PaginatedRecords(
    val page: Int,
    val perPage: Int,
    val totalItems: Int,
    val totalPages: Int,
    val items: List<Record>,
)

@Serializable(with = RecordSerializer::class)
data class Record(
    val id: String,
    @SerialName("collectionId") val collectionId: String? = null,
    @SerialName("collectionName") val collectionName: String? = null,
    val created: String? = null,
    val updated: String? = null,
    val expand: JsonObject? = null,
    val data: Map<String, JsonElement> = emptyMap()
)

object RecordSerializer : KSerializer<Record> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Record")
    override fun deserialize(decoder: Decoder): Record {
        require(decoder is JsonDecoder)
        val obj = decoder.decodeJsonElement().jsonObject
        fun js(key: String) = obj[key]?.jsonPrimitive?.contentOrNull
        val core = setOf("id","collectionId","collectionName","created","updated","expand")
        val id = obj["id"]?.jsonPrimitive?.content ?: throw SerializationException("Record.id missing")
        val data = obj.filterKeys { it !in core }
        return Record(id, js("collectionId"), js("collectionName"), js("created"), js("updated"), obj["expand"]?.jsonObject, data)
    }
    override fun serialize(encoder: Encoder, value: Record) {
        require(encoder is JsonEncoder)
        val base = buildJsonObject {
            put("id", JsonPrimitive(value.id))
            value.collectionId?.let { put("collectionId", JsonPrimitive(it)) }
            value.collectionName?.let { put("collectionName", JsonPrimitive(it)) }
            value.created?.let { put("created", JsonPrimitive(it)) }
            value.updated?.let { put("updated", JsonPrimitive(it)) }
            value.expand?.let { put("expand", it) }
            value.data.forEach { (k, v) -> put(k, v) }
        }
        encoder.encodeJsonElement(base)
    }
}

@Serializable
data class AuthResponse(val token: String, val record: Record, val expiration: Long = 0L)
