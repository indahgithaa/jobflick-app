package com.example.jobflick.features.auth.data.repository

import com.example.jobflick.core.network.Record
import com.example.jobflick.features.auth.data.remote.AuthRemote
import com.example.jobflick.features.auth.domain.model.User
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

class AuthRepository(private val remote: AuthRemote) {

    private var currentUser: User? = null
    fun currentUser(): User? = currentUser

    private fun Record.str(key: String): String? = data[key]?.jsonPrimitive?.contentOrNull

    private fun Record.toUser(): User {
        val email = str("email") ?: error("Missing 'email'")
        val name = str("name") ?: ""
        val pp = str("profilePicture")
        val role = str("role") ?: "jobseeker"
        return User(id = id, email = email, name = name, profilePicture = pp, role = role)
    }

    suspend fun signUp(email: String, password: String, name: String, role: String): Result<User> {
        return runCatching {
            remote.signUp(email, password, name, role)
            val auth = remote.signIn(email, password)
            val user = auth.record.toUser()
            currentUser = user
            user
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> =
        runCatching { remote.signIn(email, password).record.toUser() }
            .onSuccess { currentUser = it }

    suspend fun updateProfile(userId: String, name: String, profilePicture: String?): Result<User> =
        runCatching { remote.updateProfile(userId, name, profilePicture).toUser() }
            .onSuccess { currentUser = it }

    fun signOut() { currentUser = null }
}
