package com.example.jobflick.navigation
object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SELECTROLE = "select_role"

    const val SIGNUP = "signup/{role}"
    const val SIGNIN = "signin/{role}"
    const val COMPLETE_PROFILE = "complete_profile/{role}"
    const val DONE = "done/{role}"

    // Main tabs
    const val DISCOVER = "discover"
    const val ROADMAP = "roadmap"
    const val MESSAGE = "message"
    const val PROFILE = "profile"

    fun signup(role: String) = "signup/${role.lowercase()}"
    fun signin(role: String) = "signin/${role.lowercase()}"
    fun completeProfile(role: String) = "complete_profile/${role.lowercase()}"
    fun done(role: String) = "done/${role.lowercase()}"
}
