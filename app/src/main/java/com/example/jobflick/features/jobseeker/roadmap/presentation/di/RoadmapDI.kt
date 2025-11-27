package com.example.jobflick.features.roadmap.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.jobflick.features.jobseeker.roadmap.data.datasource.RoadmapRemoteDataSource
import com.example.jobflick.features.jobseeker.roadmap.data.repository.RoadmapRepositoryImpl
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.CalculateQuizScoreUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetAvailableRolesUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetRoadmapRoleUseCase
import com.example.jobflick.features.jobseeker.roadmap.presentation.viewmodel.RoadmapViewModel

@Composable
fun rememberRoadmapViewModel(): RoadmapViewModel {
    val remote = remember { RoadmapRemoteDataSource() }
    val repo = remember { RoadmapRepositoryImpl(remote) }

    return remember {
        RoadmapViewModel(
            getAvailableRolesUseCase = GetAvailableRolesUseCase(repo),
            getRoadmapRoleUseCase = GetRoadmapRoleUseCase(repo),
            calculateQuizScoreUseCase = CalculateQuizScoreUseCase(repo)
        )
    }
}
