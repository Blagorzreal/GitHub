package com.example.github.ui.navigation

sealed class Route(val route: String) {
    object Login: Route("login")
    object Profile: Route("profile")
    object Repository: Route("repo")
    object User: Route("user")

    companion object {
        const val USER_DATA = "USER_DATA"
        const val REPO_DATA = "REPO_DATA"
    }
}