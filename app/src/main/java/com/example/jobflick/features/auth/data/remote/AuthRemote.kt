package com.example.jobflick.features.auth.data.remote

import com.example.jobflick.core.network.AuthResponse
import com.example.jobflick.core.network.PocketBaseHttp
import com.example.jobflick.core.network.Record
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRemote(
    private val pb: PocketBaseHttp,
    private val usersCollection: String
) {
    suspend fun signUp(email: String, password: String, name: String, role: String): Record {
        val body = buildJsonObject {
            put("email", email)
            put("password", password)
            put("passwordConfirm", password)
            put("name", name)
            put("role", role)
        }
        return pb.create(usersCollection, body)
    }


    suspend fun signIn(email: String, password: String): AuthResponse =
        pb.authWithPassword(collection = usersCollection, identity = email, password = password)

    suspend fun updateProfile(userId: String, name: String, profilePicture: String?): Record {
        val body = buildJsonObject {
            put("name", name)
            if (profilePicture != null) put("profilePicture", profilePicture)
        }
        return pb.update(usersCollection, userId, body)
    }
}
