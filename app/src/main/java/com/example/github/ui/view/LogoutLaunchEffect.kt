package com.example.github.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.github.data.LoginSession
import com.example.github.ui.navigation.Route

@Composable
fun LogoutLaunchEffect(navController: NavHostController) {
    LoginSession.clean()

    LaunchedEffect(true) {
        Route.inclusiveNavigation(navController, Route.Login, Route.Profile)
    }
}