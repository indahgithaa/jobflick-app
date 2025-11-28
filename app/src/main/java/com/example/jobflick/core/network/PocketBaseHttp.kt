package com.example.jobflick.core.network

import android.util.Base64
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.put
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class PocketBaseHttp(
    private val baseUrl: String,
    private var authToken: String? = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2xsZWN0aW9uSWQiOiJfcGJfdXNlcnNfYXV0aF8iLCJleHAiOjE3NjQ4OTUyMzYsImlkIjoiMTkxeGtvOGFvMHo4dzFlIiwicmVmcmVzaGFibGUiOnRydWUsInR5cGUiOiJhdXRoIn0.MTyeOJGbYRuSLALY6fz9V8u9ESM1J8tTu5tR8MBEpt0",
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

    // Cache for auth response
    private var lastAuth: AuthResponse? = null

    // In-memory cache for records
    private val cache = mutableMapOf<String, Pair<Long, PaginatedRecords>>()
    private val cacheExpiry = 5 * 1000L // 3 seconds

    private fun getCacheKey(collection: String, page: Int, perPage: Int, sort: String?, filter: String?, expand: String?, fields: String?, skipTotal: Boolean?): String {
        return "$collection|$page|$perPage|$sort|$filter|$expand|$fields|$skipTotal"
    }

    private fun isCacheValid(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp < cacheExpiry
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
        val cacheKey = getCacheKey(collection, page, perPage, sort, filter, expand, fields, skipTotal)
        val cached = cache[cacheKey]
        if (cached != null && isCacheValid(cached.first)) {
            return cached.second
        }

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
        val result = json.decodeFromString(PaginatedRecords.serializer(), resp.bodyAsText())
        cache[cacheKey] = System.currentTimeMillis() to result
        return result
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

        // Compute and set expiration
        val expiration = getTokenExpiration(auth.token)
        val authWithExp = auth.copy(expiration = expiration)

        // Update the cached auth response
        lastAuth = authWithExp

        return authWithExp
    }

    suspend fun authRefresh(collection: String = "users"): AuthResponse {
        // Check if we have a valid cached auth response
        lastAuth?.let {
            val now = System.currentTimeMillis()
            // If the token is not expired, return the cached auth response
            if (it.expiration > now) return it
        }

        val resp = client.post("$baseUrl/api/collections/$collection/auth-refresh")
        val auth = json.decodeFromString(AuthResponse.serializer(), resp.bodyAsText())
        this.authToken = auth.token

        // Compute and set expiration
        val expiration = getTokenExpiration(auth.token)
        val authWithExp = auth.copy(expiration = expiration)

        // Update the cached auth response
        lastAuth = authWithExp

        return authWithExp
    }

    private fun getTokenExpiration(token: String): Long {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return 0L
            val payloadStr = parts[1]
            val padded = payloadStr + "=".repeat((4 - payloadStr.length % 4) % 4)
            val payload = String(Base64.decode(padded, Base64.URL_SAFE))
            val jsonObj = json.decodeFromString(JsonObject.serializer(), payload)
            val exp = jsonObj["exp"]?.jsonPrimitive?.int ?: 0
            exp * 1000L // convert to milliseconds
        } catch (e: Exception) {
            0L
        }
    }

    fun clearAuth() { authToken = null }
    fun clearCache() { cache.clear() }
}
