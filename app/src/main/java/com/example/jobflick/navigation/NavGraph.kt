package com.example.jobflick.navigation

import CompleteProfileScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jobflick.features.auth.presentation.JobSeekerDoneRegistScreen
import com.example.jobflick.features.auth.presentation.SignInRoute
import com.example.jobflick.features.auth.presentation.SignUpRoute
import com.example.jobflick.features.jobseeker.discover.presentation.screens.DiscoverScreen
import com.example.jobflick.features.jobseeker.profile.data.datasource.ProfileRemoteDataSource
import com.example.jobflick.features.jobseeker.profile.data.repository.ProfileRepositoryImpl
import com.example.jobflick.features.jobseeker.profile.domain.model.Job
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetJobDetailUseCase
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetJobsUseCase
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetProfileUseCase
import com.example.jobflick.features.jobseeker.profile.presentation.screens.JobDetailScreen
import com.example.jobflick.features.jobseeker.profile.presentation.screens.ProfileScreen
import com.example.jobflick.features.jobseeker.profile.presentation.screens.ProfileSettingsScreen
import com.example.jobflick.features.jobseeker.profile.presentation.screens.SeeProfileScreen
import com.example.jobflick.features.jobseeker.profile.presentation.viewmodel.ProfileViewModel
import com.example.jobflick.features.jobseeker.roadmap.data.datasource.RoadmapRemoteDataSource
import com.example.jobflick.features.jobseeker.roadmap.data.repository.RoadmapRepositoryImpl
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.ArticleDetailScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.ModuleDetailScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.QuizDoneScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.QuizScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.RoadmapOverviewScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.RoadmapScreen
import com.example.jobflick.features.onboarding.presentation.OnboardingScreen
import com.example.jobflick.features.onboarding.presentation.RoleSelectionScreen
import com.example.jobflick.features.onboarding.presentation.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.SPLASH,
    modifier: Modifier = Modifier
) {
    // =====================================================================
    // Manual DI level navigation (dipakai semua screen roadmap)
    // =====================================================================
    val roadmapRemote = remember { RoadmapRemoteDataSource() }
    val roadmapRepository = remember { RoadmapRepositoryImpl(roadmapRemote) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // ========== SPLASH ==========
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ========== ONBOARDING ==========
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Routes.SELECTROLE) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.SELECTROLE) {
            RoleSelectionScreen(
                onJobSeeker = { navController.navigate(Routes.signup("jobseeker")) },
                onRecruiter = { navController.navigate(Routes.signup("recruiter")) }
            )
        }

        // ========== AUTH ==========
        composable(
            route = Routes.SIGNUP,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "jobseeker"

            SignUpRoute(
                role = role,
                onSignedUp = {
                    navController.navigate(Routes.completeProfile(role)) {
                        popUpTo(Routes.signup(role)) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onClickSignIn = {
                    navController.navigate(Routes.signin(role)) { launchSingleTop = true }
                }
            )
        }

        composable(
            route = Routes.COMPLETE_PROFILE,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "jobseeker"

            CompleteProfileScreen(
                role = role,
                onPickPhoto = { /* TODO */ },
                onPickCV = { /* TODO */ },
                onSubmit = { _, _, _, _, _, _, _, _, _ ->
                    navController.navigate(Routes.done(role)) {
                        popUpTo(Routes.completeProfile(role)) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.SIGNIN,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "jobseeker"

            SignInRoute(
                onSignedIn = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onClickSignUp = {
                    navController.navigate(Routes.signup(role)) { launchSingleTop = true }
                }
            )
        }

        composable(
            route = Routes.DONE,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) {
            JobSeekerDoneRegistScreen(
                onStart = {
                    navController.navigate(Routes.DISCOVER) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ========== MAIN TAB: DISCOVER ==========
        composable(Routes.DISCOVER) {
            DiscoverScreen(
                onApply = { /* TODO */ },
                onSave = { /* TODO */ }
            )
        }

        // ========== MAIN TAB: ROADMAP ==========
        composable(Routes.ROADMAP) {
            RoadmapScreen(
                onRoleSelected = { roleName ->
                    navController.navigate(Routes.roadmapOverview(roleName))
                }
            )
        }

        composable(
            route = Routes.ROADMAP_OVERVIEW,
            arguments = listOf(navArgument("roleName") { type = NavType.StringType })
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""

            // Ambil roadmap untuk role tertentu dari repository (stateful, pakai remember)
            val roadmapRole = remember(roleName) {
                roadmapRepository.getRoadmapRole(roleName)
            }

            RoadmapOverviewScreen(
                role = roadmapRole,
                onBack = { navController.popBackStack() },
                onSelectModule = { module ->
                    navController.navigate(
                        Routes.roadmapModuleDetail(
                            roleName = roadmapRole.name,
                            moduleNumber = module.number
                        )
                    )
                }
            )
        }

        composable(
            route = Routes.ROADMAP_MODULE_DETAIL,
            arguments = listOf(
                navArgument("roleName") { type = NavType.StringType },
                navArgument("moduleNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""
            val moduleNumber = backStackEntry.arguments?.getInt("moduleNumber") ?: 1

            val roadmapRole = remember(roleName) {
                roadmapRepository.getRoadmapRole(roleName)
            }
            val module = remember(roadmapRole, moduleNumber) {
                roadmapRole.modules.first { it.number == moduleNumber }
            }

            ModuleDetailScreen(
                role = roadmapRole,
                module = module,
                onBack = { navController.popBackStack() },
                onOpenArticle = { articleIndex ->
                    navController.navigate(
                        Routes.roadmapArticleDetail(
                            roleName = roadmapRole.name,
                            moduleNumber = module.number,
                            articleIndex = articleIndex
                        )
                    )
                },
                onOpenQuiz = {
                    navController.navigate(
                        Routes.roadmapQuiz(
                            roleName = roadmapRole.name,
                            moduleNumber = module.number
                        )
                    )
                }
            )
        }

        composable(
            route = Routes.ROADMAP_ARTICLE_DETAIL,
            arguments = listOf(
                navArgument("roleName") { type = NavType.StringType },
                navArgument("moduleNumber") { type = NavType.IntType },
                navArgument("articleIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""
            val moduleNumber = backStackEntry.arguments?.getInt("moduleNumber") ?: 1
            val articleIndex = backStackEntry.arguments?.getInt("articleIndex") ?: 0

            val roadmapRole = remember(roleName) {
                roadmapRepository.getRoadmapRole(roleName)
            }
            val module = remember(roadmapRole, moduleNumber) {
                roadmapRole.modules.first { it.number == moduleNumber }
            }
            val article = module.articles.getOrNull(articleIndex)

            ArticleDetailScreen(
                role = roadmapRole,
                module = module,
                articleTitle = article?.title ?: "",
                articleIndex = articleIndex,
                onBack = { navController.popBackStack() },
                onOpenArticle = { nextIndex ->
                    navController.navigate(
                        Routes.roadmapArticleDetail(
                            roleName = roadmapRole.name,
                            moduleNumber = module.number,
                            articleIndex = nextIndex
                        )
                    )
                },
                onOpenQuiz = {
                    navController.navigate(
                        Routes.roadmapQuiz(
                            roleName = roadmapRole.name,
                            moduleNumber = module.number
                        )
                    )
                }
            )
        }

        composable(
            route = Routes.ROADMAP_QUIZ,
            arguments = listOf(
                navArgument("roleName") { type = NavType.StringType },
                navArgument("moduleNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""
            val moduleNumber = backStackEntry.arguments?.getInt("moduleNumber") ?: 1

            // pakai repository yang tadi sudah di-remember di NavGraph
            val roadmapRole = remember(roleName) {
                roadmapRepository.getRoadmapRole(roleName)
            }
            val module = remember(roadmapRole, moduleNumber) {
                roadmapRole.modules.first { it.number == moduleNumber }
            }

            QuizScreen(
                module = module,
                onBack = { navController.popBackStack() },
                onQuizFinished = { answers: List<Int> ->
                    val score = roadmapRepository.calculateQuizScore(
                        roleName = roadmapRole.name,
                        moduleId = module.id,
                        answers = answers
                    )

                    navController.navigate(
                        Routes.roadmapQuizDone(
                            roleName = roadmapRole.name,
                            score = score
                        )
                    ) { /* popUpTo dll */ }
                }
            )
        }


        composable(
            route = Routes.ROADMAP_QUIZ_DONE,
            arguments = listOf(
                navArgument("roleName") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""
            val score = backStackEntry.arguments?.getInt("score") ?: 0

            QuizDoneScreen(
                score = score,
                onClose = {
                    navController.navigate(Routes.roadmapOverview(roleName)) {
                        popUpTo(Routes.ROADMAP_OVERVIEW) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ========== MAIN TAB: PROFILE (jobs list) ==========
        composable(Routes.PROFILE) {

            // manual DI profile
            val remote = remember { ProfileRemoteDataSource() }
            val repository = remember { ProfileRepositoryImpl(remote) }
            val getProfile = remember { GetProfileUseCase(repository) }
            val getJobs = remember { GetJobsUseCase(repository) }
            val getJobDetail = remember { GetJobDetailUseCase(repository) }

            val viewModel = remember {
                ProfileViewModel(
                    getProfile = getProfile,
                    getJobs = getJobs,
                    getJobDetail = getJobDetail
                )
            }

            val state by viewModel.uiState.collectAsState()

            when {
                state.isLoading && state.profile == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.profile == null && state.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.errorMessage ?: "Terjadi kesalahan")
                    }
                }

                state.profile != null -> {
                    ProfileScreen(
                        profile = state.profile!!,
                        selectedTab = state.selectedTab,
                        matchJobs = state.matchJobs,
                        savedJobs = state.savedJobs,
                        appliedJobs = state.appliedJobs,
                        onTabSelected = viewModel::onTabSelected,
                        onJobClick = { job ->
                            navController.navigate(Routes.profileJobDetail(job.id))
                        },
                        onSeeProfileClick = {
                            navController.navigate(Routes.PROFILE_SEE_PROFILE)
                        },
                        onSettingsClick = {
                            navController.navigate(Routes.PROFILE_SETTINGS)
                        },
                        onLogoutClick = {
                            navController.navigate(Routes.signin("jobseeker")) {
                                popUpTo(Routes.DISCOVER) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }

        // ========== PROFILE: JOB DETAIL ==========
        composable(
            route = Routes.PROFILE_JOB_DETAIL,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            val remote = remember { ProfileRemoteDataSource() }
            val repository = remember { ProfileRepositoryImpl(remote) }
            val getJobDetail = remember { GetJobDetailUseCase(repository) }

            var job by remember { mutableStateOf<Job?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(jobId) {
                try {
                    isLoading = true
                    error = null
                    job = getJobDetail(jobId)
                } catch (e: Exception) {
                    error = e.message ?: "Terjadi kesalahan"
                } finally {
                    isLoading = false
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                error != null || job == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = error ?: "Lowongan tidak ditemukan")
                    }
                }

                else -> {
                    JobDetailScreen(
                        job = job!!,
                        onApplyClick = {
                            // TODO: apply
                        },
                        onDeleteClick = {
                            // TODO: delete from saved/applied jika perlu
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        // ========== PROFILE: SETTINGS ==========
        composable(Routes.PROFILE_SETTINGS) {
            ProfileSettingsScreen(
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE_SEE_PROFILE)
                },
                onOpenExperience = {
                    // TODO: screen pengalaman
                },
                onOpenPreference = {
                    // TODO: screen preferensi
                },
                onOpenSecurity = {
                    // TODO: screen keamanan
                },
                onOpenDeleteAccount = {
                    // TODO: dialog hapus akun
                },
                onToggleDirectContact = { _ ->
                    // TODO: simpan preferensi kontak langsung
                },
                onOpenAboutApp = {
                    // TODO
                },
                onOpenReport = {
                    // TODO
                },
                onOpenTerms = {
                    // TODO
                },
                onLogout = {
                    navController.navigate(Routes.signin("jobseeker")) {
                        popUpTo(Routes.DISCOVER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ========== PROFILE: SEE PROFILE ==========
        composable(Routes.PROFILE_SEE_PROFILE) {
            // sementara pakai data statis sesuai desain
            SeeProfileScreen(
                name = "Aulia Rahma",
                photoUrl = null,
                whatsapp = "+6281234567890",
                email = "auliarahma@gmail.com",
                domicile = "Jakarta Selatan",
                education = "Sarjana (S1) - Informatika",
                cvFileName = "CV.pdf",
                portfolioUrl = "https://bit.ly/Portofolio",
                onEditField = { /* TODO */ },
                onDeletePortfolio = { /* TODO */ },
                onSave = { navController.popBackStack() }
            )
        }
    }
}
