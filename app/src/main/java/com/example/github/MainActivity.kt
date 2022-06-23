package com.example.github

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.github.data.LoginSession
import com.example.github.ui.navigation.Route
import com.example.github.ui.screen.login.LoginScreen
import com.example.github.ui.screen.profile.ProfileScreen
import com.example.github.ui.theme.GitHubTheme

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val startDestination = if (LoginSession.isActive) Route.Profile else Route.Login

                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startDestination.route) {
                        composable(Route.Login.route) {
                            LoginScreen(navController)
                        }

                        composable(Route.Profile.route) {
                            BackHandler(true) {
                                moveTaskToBack(true)
                            }

                            ProfileScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GitHubTheme {
        LoginScreen(rememberNavController())
    }
}