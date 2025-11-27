package com.example.jobflick.core.di

import com.example.jobflick.core.network.PocketBaseHttp
import com.example.jobflick.features.auth.data.repository.AuthRepository
import com.example.jobflick.features.auth.data.remote.AuthRemote

object AppGraph {
    private val pb = PocketBaseHttp(baseUrl = "https://jobflick.pb.komangdavid.com")

    // Remotes
    private val authRemote = AuthRemote(pb, usersCollection = "users")

    // Repositories
    val authRepository = AuthRepository(authRemote)
}
