package com.example.github.ui.view

import androidx.navigation.NavHostController
import com.example.github.ui.navigation.Route

fun inclusiveNavigation(navController: NavHostController, newDestination: Route, oldDestination: Route, saveOldState: Boolean = false) {
    navController.navigate(newDestination.route) {
        popUpTo(oldDestination.route) {
            inclusive = true
            saveState = saveOldState
        }
    }
}