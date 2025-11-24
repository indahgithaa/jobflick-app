package com.example.jobflick.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jobflick.features.onboarding.presentation.OnboardingScreen
import com.example.jobflick.features.onboarding.presentation.SplashScreen
import com.example.jobflick.features.onboarding.presentation.RoleSelectionScreen
import com.example.jobflick.features.auth.presentation.*
import com.example.jobflick.features.discover.presentation.screens.DiscoverScreen
import com.example.jobflick.features.message.presentation.MessageScreen
import com.example.jobflick.features.profile.presentation.ProfileScreen
import com.example.jobflick.features.roadmap.presentation.screens.*
import com.example.jobflick.features.roadmap.data.remote.RoadmapRemoteDataSource
import com.example.jobflick.features.roadmap.data.repository.RoadmapRepositoryImpl

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.SPLASH,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // SPLASH
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

        // ONBOARDING
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

        // ROLE SELECTION (jobseeker / recruiter)
        composable(Routes.SELECTROLE) {
            RoleSelectionScreen(
                onJobSeeker = { navController.navigate(Routes.signup("jobseeker")) },
                onRecruiter = { navController.navigate(Routes.signup("recruiter")) }
            )
        }

        // SIGN UP
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

        // COMPLETE PROFILE (setelah signup)
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

        // SIGN IN
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

        // DONE (setelah complete profile)
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

        // DISCOVER (tab utama)
        composable(Routes.DISCOVER) {
            DiscoverScreen(
                onApply = { /* TODO */ },
                onSave = { /* TODO */ }
            )
        }

        // =========================
        // ROADMAP FLOW
        // =========================

        // 1. Pilih role (screen pertama tab Roadmap)
        composable(Routes.ROADMAP) {
            RoadmapScreen(
                onRoleSelected = { roleName ->
                    navController.navigate(Routes.roadmapOverview(roleName))
                }
            )
        }

        // 2. Overview role (daftar modul)
        composable(
            route = Routes.ROADMAP_OVERVIEW,
            arguments = listOf(navArgument("roleName") { type = NavType.StringType })
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""
            val repository = remember { RoadmapRepositoryImpl(RoadmapRemoteDataSource()) }
            val roadmapRole = remember(roleName) { repository.getRoadmapRole(roleName) }

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

        // 3. Detail module (pilih artikel / kuis)
        composable(
            route = Routes.ROADMAP_MODULE_DETAIL,
            arguments = listOf(
                navArgument("roleName") { type = NavType.StringType },
                navArgument("moduleNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""
            val moduleNumber = backStackEntry.arguments?.getInt("moduleNumber") ?: 1

            val repository = remember { RoadmapRepositoryImpl(RoadmapRemoteDataSource()) }
            val roadmapRole = remember(roleName) { repository.getRoadmapRole(roleName) }
            val module = remember(roadmapRole, moduleNumber) {
                roadmapRole.modules.first { it.number == moduleNumber }
            }

            ModuleDetailScreen(
                role = roadmapRole,
                module = module,
                onBack = { navController.popBackStack() },
                onOpenArticle = { articleTitle ->
                    val index = module.articles.indexOf(articleTitle).coerceAtLeast(0)
                    navController.navigate(
                        Routes.roadmapArticleDetail(
                            roleName = roadmapRole.name,
                            moduleNumber = module.number,
                            articleIndex = index
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

        // 4. Artikel
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

            val repository = remember { RoadmapRepositoryImpl(RoadmapRemoteDataSource()) }
            val roadmapRole = remember(roleName) { repository.getRoadmapRole(roleName) }
            val module = remember(roadmapRole, moduleNumber) {
                roadmapRole.modules.first { it.number == moduleNumber }
            }
            val articleTitle = module.articles.getOrNull(articleIndex) ?: ""

            ArticleDetailScreen(
                role = roadmapRole,
                module = module,
                articleTitle = articleTitle,
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


        // 5. Kuis
        composable(
            route = Routes.ROADMAP_QUIZ,
            arguments = listOf(
                navArgument("roleName") { type = NavType.StringType },
                navArgument("moduleNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("roleName") ?: ""
            val moduleNumber = backStackEntry.arguments?.getInt("moduleNumber") ?: 1

            val repository = remember { RoadmapRepositoryImpl(RoadmapRemoteDataSource()) }
            val roadmapRole = remember(roleName) { repository.getRoadmapRole(roleName) }
            val module = remember(roadmapRole, moduleNumber) {
                roadmapRole.modules.first { it.number == moduleNumber }
            }

            QuizScreen(
                module = module,
                onBack = { navController.popBackStack() },
                onQuizFinished = { score ->
                    navController.navigate(
                        Routes.roadmapQuizDone(
                            roleName = roadmapRole.name,
                            score = score
                        )
                    ) {
                        popUpTo(Routes.roadmapQuiz(roleName, moduleNumber)) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 6. Done (skor)
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

        // MESSAGE
        composable(Routes.MESSAGE) {
            MessageScreen()
        }

        // PROFILE
        composable(Routes.PROFILE) {
            ProfileScreen()
        }
    }
}
