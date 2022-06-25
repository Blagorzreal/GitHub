package com.example.github.ui.navigation

sealed class Route(val route: String) {
    object Login: Route("login")
    object Profile: Route("profile")
    object Repository: Route("repository")

    companion object {
        const val REPO_DATA = "REPO_DATA"
    }
}