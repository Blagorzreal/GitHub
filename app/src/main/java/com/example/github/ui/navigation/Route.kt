package com.example.github.ui.navigation

sealed class Route(val route: String) {
    object Login: Route("login")
    object Profile: Route("profile")
}