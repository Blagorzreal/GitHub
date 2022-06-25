package com.example.github.ui.navigation

import com.example.github.util.Constants

sealed class Route(val route: String) {
    object Login: Route("login")
    object Profile: Route("profile")
    object Repository: Route("repository")

    companion object {
        const val REPO_DATA = "REPO_DATA"
        private fun getParameter(parameter: String?) = parameter ?: Constants.EMPTY_STRING
    }
}