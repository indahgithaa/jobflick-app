package com.example.jobflick.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SELECTROLE = "select_role"

    // AUTH GENERIC
    const val SIGNUP = "signup/{role}"
    fun signup(role: String) = "signup/$role"

    const val COMPLETE_PROFILE = "completeProfile/{role}"
    fun completeProfile(role: String) = "completeProfile/$role"

    const val SIGNIN = "signin/{role}"
    fun signin(role: String) = "signin/$role"

    const val DONE = "done/{role}"
    fun done(role: String) = "done/$role"

    // MAIN TABS (JOBSEEKER)
    const val DISCOVER = "discover"
    const val ROADMAP = "roadmap"
    const val MESSAGE = "message"
    const val PROFILE = "profile"

    // DISCOVER EXTRA
    const val DISCOVER_JOB_DETAIL = "discoverJobDetail/{jobId}"
    fun discoverJobDetail(jobId: String) = "discoverJobDetail/$jobId"

    const val JOB_MATCH = "jobMatch/{jobId}"
    fun jobMatch(jobId: String) = "jobMatch/$jobId"

    // PROFILE EXTRA
    const val PROFILE_SETTINGS = "profileSettings"
    const val PROFILE_SEE_PROFILE = "profileSeeProfile"

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

    // RECRUITER AUTH LANJUTAN
    const val RECRUITER_COMPANY_PROFILE = "recruiterCompanyProfile"
    const val RECRUITER_PROFILE_INFO = "recruiterProfileInfo"

    // RECRUITER MAIN TABS
    const val RECRUITER_DASHBOARD = "recruiterDashboard"
    const val RECRUITER_DISCOVER = "recruiterDiscover"
    const val RECRUITER_PROFILE = "recruiterProfile"

    // RECRUITER JOB POSTING
    const val RECRUITER_POST_JOB = "recruiterPostJob"

}
