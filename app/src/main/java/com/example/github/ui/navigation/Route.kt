package com.example.github.ui.navigation

import androidx.navigation.NavHostController
import com.example.github.data.LoginSession
import com.example.github.data.data.UserData

sealed class Route(val route: String) {
    object Login: Route("login")
    object Profile: Route("profile")
    object Repository: Route("repository")
    object User: Route("user")
    object Search: Route("search")

    companion object {
        const val USER_DATA = "USER_DATA"
        const val REPO_DATA = "REPO_DATA"

        fun userScreenNavigation(
            navController: NavHostController,
            userData: UserData) {

            if (userData.id == LoginSession.userData?.id)
                inclusiveNavigation(navController, Profile, Profile)
            else
                navController.navigate(User.route)

            navController.currentBackStackEntry?.arguments?.putParcelable(USER_DATA, userData)
        }

        fun inclusiveNavigation(navController: NavHostController, newDestination: Route, oldDestination: Route, saveOldState: Boolean = false) {
            navController.navigate(newDestination.route) {
                popUpTo(oldDestination.route) {
                    inclusive = true
                    saveState = saveOldState
                }
            }
        }
    }
}