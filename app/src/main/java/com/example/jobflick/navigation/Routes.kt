package com.example.jobflick.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SELECTROLE = "select_role"

    // AUTH
    const val SIGNUP = "signup/{role}"
    fun signup(role: String) = "signup/$role"

    const val COMPLETE_PROFILE = "completeProfile/{role}"
    fun completeProfile(role: String) = "completeProfile/$role"

    const val SIGNIN = "signin/{role}"
    fun signin(role: String) = "signin/$role"

    const val DONE = "done/{role}"
    fun done(role: String) = "done/$role"

    // MAIN TABS
    const val DISCOVER = "discover"
    const val ROADMAP = "roadmap"
    const val MESSAGE = "message"
    const val PROFILE = "profile"

    // PROFILE EXTRA
    const val PROFILE_SETTINGS = "profileSettings"
    const val PROFILE_SEE_PROFILE = "profileSeeProfile"

    // PROFILE JOB DETAIL
    const val PROFILE_JOB_DETAIL = "profileJobDetail/{jobId}"
    fun profileJobDetail(jobId: String) = "profileJobDetail/$jobId"

    // ROADMAP FLOW
    const val ROADMAP_OVERVIEW = "roadmapOverview/{roleName}"
    fun roadmapOverview(roleName: String) = "roadmapOverview/$roleName"

    const val ROADMAP_MODULE_DETAIL = "roadmapModuleDetail/{roleName}/{moduleNumber}"
    fun roadmapModuleDetail(roleName: String, moduleNumber: Int) =
        "roadmapModuleDetail/$roleName/$moduleNumber"

    const val ROADMAP_ARTICLE_DETAIL =
        "roadmapArticleDetail/{roleName}/{moduleNumber}/{articleIndex}"
    fun roadmapArticleDetail(roleName: String, moduleNumber: Int, articleIndex: Int) =
        "roadmapArticleDetail/$roleName/$moduleNumber/$articleIndex"

    const val ROADMAP_QUIZ = "roadmapQuiz/{roleName}/{moduleNumber}"
    fun roadmapQuiz(roleName: String, moduleNumber: Int) =
        "roadmapQuiz/$roleName/$moduleNumber"

    const val ROADMAP_QUIZ_DONE = "roadmapQuizDone/{roleName}/{score}"
    fun roadmapQuizDone(roleName: String, score: Int) =
        "roadmapQuizDone/$roleName/$score"
}
