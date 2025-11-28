package com.example.jobflick.core.global

import com.example.jobflick.core.network.PocketBaseHttp
import com.example.jobflick.features.auth.data.repository.AuthRepository
import com.example.jobflick.features.auth.data.remote.AuthRemote
import com.example.jobflick.features.jobseeker.profile.data.datasource.ProfileRemoteDataSource
import com.example.jobflick.features.jobseeker.profile.data.repository.ProfileRepositoryImpl
import com.example.jobflick.features.jobseeker.discover.data.datasource.DiscoverRemoteDataSource
import com.example.jobflick.features.jobseeker.discover.data.repository.DiscoverRepositoryImpl

object AppGraph {
    // Make pb public so it can be accessed from NavGraph.
    val pb = PocketBaseHttp(baseUrl = "https://jobflick.pb.komangdavid.com")

    // Remotes
    private val authRemote = AuthRemote(pb, usersCollection = "users")
    private val profileRemote = ProfileRemoteDataSource(pb, profilesCollection = "users", jobsCollection = "jobs")
    val discoverRemote = DiscoverRemoteDataSource(pb, profilesCollection = "users", jobsCollection = "jobs", jobCategoriesCollection = "jobCategories")

    // Repositories
    val authRepository = AuthRepository(authRemote)
    val profileRepository = ProfileRepositoryImpl(profileRemote)
    val discoverRepository = DiscoverRepositoryImpl(discoverRemote)
}
