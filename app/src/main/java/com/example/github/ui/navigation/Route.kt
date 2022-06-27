package com.example.github.ui.navigation

sealed class Route(val route: String) {
    object Login: Route("login")
    object Profile: Route("starred")
    object Repository: Route("repository")
    object User: Route("user")
    object Search: Route("search")

    companion object {
        const val USER_DATA = "USER_DATA"
        const val REPO_DATA = "REPO_DATA"
    }
}