package com.example.jobflick.features.jobseeker.roadmap.presentation.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.jobflick.core.global.AppGraph
import com.example.jobflick.features.jobseeker.roadmap.data.datasource.RoadmapRemoteDataSource
import com.example.jobflick.features.jobseeker.roadmap.data.repository.RoadmapRepositoryImpl
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.CalculateQuizScoreUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetAvailableRolesUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetRoadmapRoleUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.MarkArticleAsReadUseCase
import com.example.jobflick.features.jobseeker.roadmap.presentation.viewmodel.RoadmapViewModel

@Composable
fun rememberRoadmapViewModel(): RoadmapViewModel {
    val remote = remember { RoadmapRemoteDataSource(
        pb = AppGraph.pb,
        roadmapsCollection = "roadmaps",
        modulesCollection = "modules",
        articlesCollection = "articles",
        quizzesCollection = "quizzes",
        questionsCollection = "questions",
        optionsCollection = "options",
        moduleProgressCollection = "moduleProgress",
        quizProgressCollection = "quizProgress",
        articleProgressCollection = "articleProgress"
    ) }
    val repo = remember { RoadmapRepositoryImpl(remote) }

    return remember {
        RoadmapViewModel(
            getAvailableRolesUseCase = GetAvailableRolesUseCase(repo),
            getRoadmapRoleUseCase = GetRoadmapRoleUseCase(repo),
            calculateQuizScoreUseCase = CalculateQuizScoreUseCase(repo),
            markArticleAsReadUseCase = MarkArticleAsReadUseCase(repo)
        )
    }
}
