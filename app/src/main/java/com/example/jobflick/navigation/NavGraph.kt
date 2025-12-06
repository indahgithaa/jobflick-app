package com.example.jobflick.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jobflick.core.global.AppGraph
import com.example.jobflick.features.auth.presentation.CompleteProfileScreen

// ====== AUTH JOBSEEKER ======
import com.example.jobflick.features.auth.presentation.JobSeekerDoneRegistScreen
import com.example.jobflick.features.auth.presentation.SignInRoute
import com.example.jobflick.features.auth.presentation.SignUpRoute

// ====== JOBSEEKER DISCOVER ======
import com.example.jobflick.features.jobseeker.discover.data.datasource.DiscoverRemoteDataSource
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.discover.presentation.screens.DiscoverScreen
import com.example.jobflick.features.jobseeker.discover.presentation.screens.DiscoverJobDetailScreen
import com.example.jobflick.features.jobseeker.discover.presentation.screens.JobMatchScreen

// ====== JOBSEEKER PROFILE ======
import com.example.jobflick.features.jobseeker.profile.data.datasource.ProfileRemoteDataSource
import com.example.jobflick.features.jobseeker.profile.data.repository.ProfileRepositoryImpl
import com.example.jobflick.features.jobseeker.profile.domain.model.Job
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetJobDetailUseCase
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetJobsUseCase
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetProfileUseCase
import com.example.jobflick.features.jobseeker.profile.presentation.screens.JobDetailScreen
import com.example.jobflick.features.jobseeker.profile.presentation.screens.ProfileScreen
import com.example.jobflick.features.jobseeker.profile.presentation.screens.ProfileSettingsScreen
import com.example.jobflick.features.jobseeker.profile.presentation.screens.SeeProfileScreen
import com.example.jobflick.features.jobseeker.profile.presentation.viewmodel.ProfileViewModel

// ====== JOBSEEKER ROADMAP ======
import com.example.jobflick.features.jobseeker.roadmap.data.datasource.RoadmapRemoteDataSource
import com.example.jobflick.features.jobseeker.roadmap.data.repository.RoadmapRepositoryImpl
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.ArticleDetailScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.ModuleDetailScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.QuizDoneScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.QuizScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.RoadmapOverviewScreen
import com.example.jobflick.features.jobseeker.roadmap.presentation.screens.RoadmapScreen

// ====== ONBOARDING ======
import com.example.jobflick.features.onboarding.presentation.OnboardingScreen
import com.example.jobflick.features.onboarding.presentation.RoleSelectionScreen
import com.example.jobflick.features.onboarding.presentation.SplashScreen

// ====== RECRUITER AUTH SCREENS ======
import com.example.jobflick.features.recruiter.auth.presentation.screens.RecruiterCompanyProfileScreen
import com.example.jobflick.features.recruiter.auth.presentation.screens.RecruiterDoneScreen
import com.example.jobflick.features.recruiter.auth.presentation.screens.RecruiterProfileInfoScreen
import com.example.jobflick.features.recruiter.auth.presentation.screens.RecruiterSignInScreen
import com.example.jobflick.features.recruiter.auth.presentation.screens.RecruiterSignUpRoute

// ====== RECRUITER MAIN TABS SCREENS ======
// ganti: gunakan Route (yang sudah setup ViewModel + Repository)
import com.example.jobflick.features.recruiter.dashboard.presentation.RecruiterDashboardRoute
import com.example.jobflick.features.recruiter.discover.presentation.screens.RecruiterDiscoverScreen
import com.example.jobflick.features.recruiter.postjob.presentation.RecruiterPostJobRoute
import com.example.jobflick.features.recruiter.profile.domain.model.Candidate
import com.example.jobflick.features.recruiter.profile.domain.model.CandidateCategory
import com.example.jobflick.features.recruiter.profile.domain.model.RecruiterProfile
import com.example.jobflick.features.recruiter.profile.presentation.RecruiterProfileRoute
import com.example.jobflick.features.recruiter.profile.presentation.screens.RecruiterProfileScreen
import kotlinx.coroutines.launch
import com.example.jobflick.core.ui.components.LogoutConfirmationDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.SPLASH,
    modifier: Modifier = Modifier
) {
    val roadmapRemote = remember { RoadmapRemoteDataSource(
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

        // ========== AUTH: SIGNUP ==========
        composable(
            route = Routes.SIGNUP,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "jobseeker"

            if (role == "recruiter") {
                RecruiterSignUpRoute(
                    onSignedUp = {
                        navController.navigate(Routes.RECRUITER_COMPANY_PROFILE) {
                            popUpTo(Routes.signup(role)) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onClickSignIn = {
                        navController.navigate(Routes.signin("recruiter")) { launchSingleTop = true }
                    }
                )
            } else {
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
        }

        // ========== AUTH: COMPLETE PROFILE JOBSEEKER ==========
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

        // ========== AUTH: RECRUITER COMPANY PROFILE ==========
        composable(Routes.RECRUITER_COMPANY_PROFILE) {
            RecruiterCompanyProfileScreen(
                onPickCompanyLogo = { /* TODO */ },
                onSubmit = { _, _, _, _, _, _, _ ->
                    navController.navigate(Routes.RECRUITER_PROFILE_INFO)
                }
            )
        }

        // ========== AUTH: RECRUITER PROFILE INFO ==========
        composable(Routes.RECRUITER_PROFILE_INFO) {
            RecruiterProfileInfoScreen(
                onPickPhoto = { /* TODO */ },
                onSubmit = { _, _, _, _, _ ->
                    navController.navigate(Routes.done("recruiter")) {
                        popUpTo(Routes.RECRUITER_PROFILE_INFO) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ========== AUTH: SIGNIN ==========
        composable(
            route = Routes.SIGNIN,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "jobseeker"

            if (role == "recruiter") {
                RecruiterSignInScreen(
                    onSubmit = { _, _ ->
                        navController.navigate(Routes.RECRUITER_DASHBOARD) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onClickSignUp = {
                        navController.navigate(Routes.signup("recruiter")) { launchSingleTop = true }
                    }
                )
            } else {
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
        }

        // ========== AUTH: DONE ==========
        composable(
            route = Routes.DONE,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "jobseeker"

            if (role == "recruiter") {
                RecruiterDoneScreen(
                    onStart = {
                        navController.navigate(Routes.RECRUITER_DASHBOARD) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            } else {
                JobSeekerDoneRegistScreen(
                    onStart = {
                        navController.navigate(Routes.DISCOVER) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // ========== MAIN TAB: DISCOVER (JOBSEEKER) ==========
        composable(Routes.DISCOVER) {
            val discoverRepository = remember { AppGraph.discoverRepository }
            val scope = rememberCoroutineScope()
            var hasApplied by remember { mutableStateOf(false) }

            DiscoverScreen(
                onApply = { job ->
                    scope.launch {
                        discoverRepository.postJobCategory(job.id, if (job.company == "Tokopedia") JobCategory.MATCH else JobCategory.APPLIED)
                        hasApplied = true
                    }
                    if (job.company == "Tokopedia") {
                        navController.navigate(Routes.jobMatch(job.id))
                    }
                },
                onSave = { job ->
                    scope.launch {
                        discoverRepository.postJobCategory(job.id, JobCategory.SAVED)
                    }
                },
                onOpenDetail = { job ->
                    navController.navigate(Routes.discoverJobDetail(job.id))
                },
                onOpenFilter = {
                    // TODO
                }
            )
        }

        // ========== DISCOVER: JOB DETAIL ==========
        composable(
            route = Routes.DISCOVER_JOB_DETAIL,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            val remote = remember { AppGraph.discoverRemote }
            var job by remember { mutableStateOf<JobPosting?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(jobId) {
                try {
                    isLoading = true
                    error = null
                    val jobs = remote.getDiscoverJobs()
                    job = jobs.firstOrNull { it.id == jobId }
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
                    ) { Text(text = error ?: "Lowongan tidak ditemukan") }
                }

                else -> {
                    DiscoverJobDetailScreen(
                        job = job!!,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }

        // ========== DISCOVER: JOB MATCH ==========
        composable(
            route = Routes.JOB_MATCH,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            val remote = remember { AppGraph.discoverRemote }
            var job by remember { mutableStateOf<JobPosting?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(jobId) {
                try {
                    isLoading = true
                    error = null
                    val jobs = remote.getAllJobs().filter { it.company == "Tokopedia" }
                    job = jobs.firstOrNull { it.id == jobId }
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
                    ) { Text(text = error ?: "Lowongan tidak ditemukan") }
                }

                else -> {
                    JobMatchScreen(
                        job = job!!,
                        onClose = { navController.popBackStack() }
                    )
                }
            }
        }

        // ========== MAIN TAB: ROADMAP (JOBSEEKER) ==========
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

            var roadmapRole by remember { mutableStateOf<RoadmapRole?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(roleName) {
                try {
                    isLoading = true
                    error = null
                    roadmapRole = roadmapRepository.getRoadmapRole(roleName)
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

                error != null || roadmapRole == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(text = error ?: "Roadmap tidak ditemukan") }
                }

                else -> {
                    RoadmapOverviewScreen(
                        role = roadmapRole!!,
                        onBack = { navController.popBackStack() },
                        onSelectModule = { module ->
                            navController.navigate(
                                Routes.roadmapModuleDetail(
                                    roleName = roadmapRole!!.name,
                                    moduleNumber = module.number
                                )
                            )
                        }
                    )
                }
            }
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

            var roadmapRole by remember { mutableStateOf<RoadmapRole?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(roleName) {
                try {
                    isLoading = true
                    error = null
                    roadmapRole = roadmapRepository.getRoadmapRole(roleName)
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

                error != null || roadmapRole == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(text = error ?: "Roadmap tidak ditemukan") }
                }

                else -> {
                    val module = roadmapRole!!.modules.first { it.number == moduleNumber }

                    ModuleDetailScreen(
                        role = roadmapRole!!,
                        module = module,
                        onBack = { navController.popBackStack() },
                        onOpenArticle = { articleIndex ->
                            navController.navigate(
                                Routes.roadmapArticleDetail(
                                    roleName = roadmapRole!!.name,
                                    moduleNumber = module.number,
                                    articleIndex = articleIndex
                                )
                            )
                        },
                        onOpenQuiz = {
                            navController.navigate(
                                Routes.roadmapQuiz(
                                    roleName = roadmapRole!!.name,
                                    moduleNumber = module.number
                                )
                            )
                        }
                    )
                }
            }
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

            var roadmapRole by remember { mutableStateOf<RoadmapRole?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(roleName) {
                try {
                    isLoading = true
                    error = null
                    roadmapRole = roadmapRepository.getRoadmapRole(roleName)
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

                error != null || roadmapRole == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(text = error ?: "Roadmap tidak ditemukan") }
                }

                else -> {
                    val module = roadmapRole!!.modules.first { it.number == moduleNumber }
                    val article = module.articles.getOrNull(articleIndex)
                    val scope = rememberCoroutineScope()

                    ArticleDetailScreen(
                        role = roadmapRole!!,
                        module = module,
                        articleTitle = article?.title ?: "",
                        articleIndex = articleIndex,
                        onBack = { navController.popBackStack() },
                        onOpenArticle = { nextIndex ->
                            navController.navigate(
                                Routes.roadmapArticleDetail(
                                    roleName = roadmapRole!!.name,
                                    moduleNumber = module.number,
                                    articleIndex = nextIndex
                                )
                            )
                        },
                        onOpenQuiz = {
                            navController.navigate(
                                Routes.roadmapQuiz(
                                    roleName = roadmapRole!!.name,
                                    moduleNumber = module.number
                                )
                            )
                        },
                        onArticleRead = { articleId ->
                            scope.launch {
                                roadmapRepository.markArticleAsRead(articleId)
                            }
                        }
                    )
                }
            }
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

            var roadmapRole by remember { mutableStateOf<RoadmapRole?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(roleName) {
                try {
                    isLoading = true
                    error = null
                    roadmapRole = roadmapRepository.getRoadmapRole(roleName)
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

                error != null || roadmapRole == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(text = error ?: "Roadmap tidak ditemukan") }
                }

                else -> {
                    val module = roadmapRole!!.modules.first { it.number == moduleNumber }
                    val scope = rememberCoroutineScope()

                    QuizScreen(
                        module = module,
                        onBack = { navController.popBackStack() },
                        onQuizFinished = { answers: List<Int> ->
                            scope.launch {
                                val score = roadmapRepository.calculateQuizScore(
                                    roleName = roadmapRole!!.name,
                                    moduleId = module.id,
                                    answers = answers
                                )

                                navController.navigate(
                                    Routes.roadmapQuizDone(
                                        roleName = roadmapRole!!.name,
                                        score = score
                                    )
                                )
                            }
                        }
                    )
                }
            }
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

        // ========== PROFILE (JOBSEEKER) ==========
        composable(Routes.PROFILE) {
            var showLogoutDialog by remember { mutableStateOf(false) }

            val remote = remember { ProfileRemoteDataSource(AppGraph.pb, "users", "jobs") }
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
                    ) { CircularProgressIndicator() }
                }

                state.profile == null && state.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(text = state.errorMessage ?: "Terjadi kesalahan") }
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
                        onLogoutClick = { showLogoutDialog = true }
                    )

                    if (showLogoutDialog) {
                        LogoutConfirmationDialog(
                            onDismiss = { showLogoutDialog = false },
                            onConfirm = {
                                showLogoutDialog = false
                                AppGraph.pb.clearAuth()
                                navController.navigate(Routes.ONBOARDING) {
                                    popUpTo(Routes.SPLASH) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }

        composable(
            route = Routes.PROFILE_JOB_DETAIL,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            val remote = remember { ProfileRemoteDataSource(AppGraph.pb, "users", "jobs") }
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
                    ) { Text(text = error ?: "Lowongan tidak ditemukan") }
                }

                else -> {
                    JobDetailScreen(
                        job = job!!,
                        onApplyClick = { /* TODO */ },
                        onDeleteClick = { navController.popBackStack() }
                    )
                }
            }
        }

        composable(Routes.PROFILE_SETTINGS) {
            var showLogoutDialog by remember { mutableStateOf(false) }

            ProfileSettingsScreen(
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE_SEE_PROFILE)
                },
                onOpenExperience = { /* TODO */ },
                onOpenPreference = { /* TODO */ },
                onOpenSecurity = { /* TODO */ },
                onOpenDeleteAccount = { /* TODO */ },
                onToggleDirectContact = { _ -> /* TODO */ },
                onOpenAboutApp = { /* TODO */ },
                onOpenReport = { /* TODO */ },
                onOpenTerms = { /* TODO */ },
                onLogout = { showLogoutDialog = true }
            )

            if (showLogoutDialog) {
                LogoutConfirmationDialog(
                    onDismiss = { showLogoutDialog = false },
                    onConfirm = {
                        showLogoutDialog = false
                        AppGraph.pb.clearAuth()
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable(Routes.PROFILE_SEE_PROFILE) {
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

        // ========== RECRUITER MAIN TABS ==========
        composable(Routes.RECRUITER_DASHBOARD) {
            RecruiterDashboardRoute(
                currentRoute = Routes.RECRUITER_DASHBOARD,
                onTabSelected = { dest ->
                    if (dest != Routes.RECRUITER_DASHBOARD) {
                        navController.navigate(dest) { launchSingleTop = true }
                    }
                },
                onAddJobClick = {
                    navController.navigate(Routes.RECRUITER_POST_JOB)
                },
                onJobClick = { jobId ->
                    // nanti kalau ada detail, navigate ke sana
                    // navController.navigate(Routes.recruiterJobDetail(jobId))
                }
            )
        }

        composable(Routes.RECRUITER_DISCOVER) {
            RecruiterDiscoverScreen(
                currentRoute = Routes.RECRUITER_DISCOVER,
                onTabSelected = { dest ->
                    if (dest != Routes.RECRUITER_DISCOVER) {
                        navController.navigate(dest) { launchSingleTop = true }
                    }
                }
            )
        }

        // ========== RECRUITER PROFILE ==========
        composable(Routes.RECRUITER_PROFILE) {
            var showLogoutDialog by remember { mutableStateOf(false) }

            RecruiterProfileRoute(
                currentRoute = Routes.RECRUITER_PROFILE,
                onTabSelected = { dest ->
                    if (dest != Routes.RECRUITER_PROFILE) {
                        navController.navigate(dest) { launchSingleTop = true }
                    }
                },
                onLogoutToSignIn = { showLogoutDialog = true }
            )

            if (showLogoutDialog) {
                LogoutConfirmationDialog(
                    onDismiss = { showLogoutDialog = false },
                    onConfirm = {
                        showLogoutDialog = false
                        AppGraph.pb.clearAuth()
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }


        // ========== RECRUITER POST JOB ==========
            composable(Routes.RECRUITER_POST_JOB) {
                RecruiterPostJobRoute(
                    currentRoute = Routes.RECRUITER_DASHBOARD, // ikon Dashboard tetap aktif
                    onTabSelected = { dest ->
                        if (dest != Routes.RECRUITER_DASHBOARD) {
                            navController.navigate(dest) { launchSingleTop = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onSubmitSuccess = {
                        // setelah sukses, balik ke dashboard
                        navController.popBackStack()  // pop post job
                    }
                )
            }
        }
    }
