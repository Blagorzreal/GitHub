package com.example.github.ui.navigation

import com.example.github.data.data.RepoData
import com.example.github.util.Constants

sealed class Route(val route: String) {
    object Login: Route("login")
    object Profile: Route("profile")
    object Repository: Route("repository")

    companion object {
        const val OWNER = "owner"
        const val REPO = "repo"

        fun getRepositoryQuery(owner: String, repo: String) =
            "${Repository.route}?$OWNER=${getParameter(owner)}&$REPO=${getParameter(repo)}"

        private fun getParameter(parameter: String?) = parameter ?: Constants.EMPTY_STRING
    }
}