package com.example.github

import android.content.Context
import android.os.Bundle
import android.widget.Toast
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
import com.example.github.data.data.RepoData
import com.example.github.ui.navigation.Route
import com.example.github.ui.navigation.Route.Companion.REPO_DATA
import com.example.github.ui.screen.login.LoginScreen
import com.example.github.ui.screen.ProfileScreen
import com.example.github.ui.screen.RepositoryScreen
import com.example.github.ui.theme.GitHubTheme
import com.example.github.util.log.AppLogger

class MainActivity: ComponentActivity() {
    companion object {
        private const val TAG = "Main activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val startDestination =
                        if (LoginSession.isActive)
                            Route.Profile
                        else
                            Route.Login

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = startDestination.route
                    ) {
                        composable(Route.Login.route) {
                            LoginScreen(navController)
                        }

                        composable(Route.Profile.route) {
                            BackHandler(true) {
                                moveTaskToBack(true)
                            }

                            val userData = LoginSession.userData
                            if (userData != null)
                                ProfileScreen(navController, userData)
                        }

                        composable(route = Route.Repository.route) {
                            val repoData = it.savedStateHandle.get<RepoData>(REPO_DATA)
                            if (repoData != null)
                                RepositoryScreen(navController, repoData)
                            else {
                                showCommonError(this@MainActivity)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showCommonError(context: Context) {
        Toast.makeText(
            context,
            getString(R.string.common_error),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onResume() {
        super.onResume()
        AppLogger.log(TAG, "Resume")
    }

    override fun onPause() {
        AppLogger.log(TAG, "Pause")
        super.onPause()
    }

    override fun onDestroy() {
        AppLogger.log(TAG, "Destroy")
        super.onDestroy()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GitHubTheme {
        LoginScreen(rememberNavController())
    }
}