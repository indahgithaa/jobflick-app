package com.example.jobflick.core.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.put

class PocketBaseHttp(
    private val baseUrl: String,
    private var authToken: String? = null,
) {
    internal val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val client = HttpClient(CIO) {
        expectSuccess = true
        install(ContentNegotiation) { json(this@PocketBaseHttp.json) }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) { Log.d("KtorPB", message) }
            }
        }
        defaultRequest {
            authToken?.let { header(HttpHeaders.Authorization, it) }
        }
    }

    suspend fun getList(
        collection: String,
        page: Int = 1,
        perPage: Int = 30,
        sort: String? = null,
        filter: String? = null,
        expand: String? = null,
        fields: String? = null,
        skipTotal: Boolean? = null,
    ): PaginatedRecords {
        val resp = client.get("$baseUrl/api/collections/$collection/records") {
            url {
                parameters.append("page", "$page")
                parameters.append("perPage", "$perPage")
                sort?.let { parameters.append("sort", it) }
                filter?.let { parameters.append("filter", it) }
                expand?.let { parameters.append("expand", it) }
                fields?.let { parameters.append("fields", it) }
                skipTotal?.let { parameters.append("skipTotal", if (it) "1" else "0") }
            }
        }
        return json.decodeFromString(PaginatedRecords.serializer(), resp.bodyAsText())
    }

    suspend fun getFullList(
        collection: String,
        perPage: Int = 200,
        sort: String? = "-created",
        filter: String? = null,
        expand: String? = null,
        fields: String? = null,
    ): List<Record> {
        val out = mutableListOf<Record>()
        var page = 1
        while (true) {
            val chunk = getList(collection, page, perPage, sort, filter, expand, fields, skipTotal = true)
            out += chunk.items
            if (chunk.items.isEmpty() || chunk.items.size < perPage) break
            page++
        }
        return out
    }

    suspend fun getOne(collection: String, recordId: String, expand: String? = null, fields: String? = null): Record {
        val resp = client.get("$baseUrl/api/collections/$collection/records/$recordId") {
            url {
                expand?.let { parameters.append("expand", it) }
                fields?.let { parameters.append("fields", it) }
            }
        }
        return json.decodeFromString(Record.serializer(), resp.bodyAsText())
    }

    suspend fun create(collection: String, body: kotlinx.serialization.json.JsonObject,
                       expand: String? = null, fields: String? = null): Record {
        val resp = client.post("$baseUrl/api/collections/$collection/records") {
            contentType(ContentType.Application.Json)
            url {
                expand?.let { parameters.append("expand", it) }
                fields?.let { parameters.append("fields", it) }
            }
            setBody(body)
        }
        return json.decodeFromString(Record.serializer(), resp.bodyAsText())
    }

    suspend fun update(collection: String, recordId: String, body: kotlinx.serialization.json.JsonObject,
                       expand: String? = null, fields: String? = null): Record {
        val resp = client.patch("$baseUrl/api/collections/$collection/records/$recordId") {
            contentType(ContentType.Application.Json)
            url {
                expand?.let { parameters.append("expand", it) }
                fields?.let { parameters.append("fields", it) }
            }
            setBody(body)
        }
        return json.decodeFromString(Record.serializer(), resp.bodyAsText())
    }

    suspend fun delete(collection: String, recordId: String) {
        client.delete("$baseUrl/api/collections/$collection/records/$recordId")
    }

    suspend fun authWithPassword(
        collection: String = "users",
        identity: String,
        password: String,
        identityField: String? = null,
        expand: String? = null,
        fields: String? = null,
    ): AuthResponse {
        val payload = kotlinx.serialization.json.buildJsonObject {
            put("identity", identity); put("password", password); identityField?.let { put("identityField", it) }
        }
        val resp = client.post("$baseUrl/api/collections/$collection/auth-with-password") {
            contentType(ContentType.Application.Json)
            url { expand?.let { parameters.append("expand", it) }; fields?.let { parameters.append("fields", it) } }
            setBody(payload)
        }
        val auth = json.decodeFromString(AuthResponse.serializer(), resp.bodyAsText())
        this.authToken = auth.token
        return auth
    }

    suspend fun authRefresh(collection: String = "users"): AuthResponse {
        val resp = client.post("$baseUrl/api/collections/$collection/auth-refresh")
        val auth = json.decodeFromString(AuthResponse.serializer(), resp.bodyAsText())
        this.authToken = auth.token
        return auth
    }

    fun clearAuth() { authToken = null }
}
