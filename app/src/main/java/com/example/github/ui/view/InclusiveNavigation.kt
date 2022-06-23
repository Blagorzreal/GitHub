package com.example.github.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.github.ui.navigation.Route

@Composable
fun InclusiveNavigation(
    navController: NavHostController,
    newDestination: Route,
    oldDestination: Route,
    saveOldState: Boolean = false
) {
    LaunchedEffect(true) {
        navController.navigate(newDestination.route) {
            popUpTo(oldDestination.route) {
                inclusive = true
                saveState = saveOldState
            }
        }
    }
}