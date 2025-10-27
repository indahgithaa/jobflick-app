package com.example.jobflick.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SELECTROLE = "select_role"

    // Auth with role
    const val SIGNUP = "signup/{role}"
    const val SIGNIN = "signin/{role}"

    fun signup(role: String) = "signup/${role.lowercase()}"
    fun signin(role: String) = "signin/${role.lowercase()}"
}
