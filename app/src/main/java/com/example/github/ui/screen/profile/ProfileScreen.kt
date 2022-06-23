package com.example.github.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.vm.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userData: UserData,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userData))) {

    Column {
        Text("Hi, ${userData.username}")

        Button(onClick = profileViewModel::logOut) {
            Text("Logout")
        }
    }

    HandleLogout(navController, profileViewModel.isLoggedOut.collectAsState())
}

@Composable
private fun HandleLogout(navController: NavHostController, isLoggedOut: State<Boolean>) {
    if (isLoggedOut.value) {
        LaunchedEffect(true) {
            navController.navigate(Route.Login.route) {
                popUpTo(Route.Profile.route) {
                    inclusive = true
                    saveState = false
                }
            }
        }
    }
}