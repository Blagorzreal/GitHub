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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.github.data.LoginSession
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.ui.navigation.Route.Companion.REPO_DATA
import com.example.github.ui.navigation.Route.Companion.USER_DATA
import com.example.github.ui.screen.login.LoginScreen
import com.example.github.ui.screen.ProfileScreen
import com.example.github.ui.screen.RepositoryScreen
import com.example.github.ui.screen.SearchScreen
import com.example.github.ui.screen.UserScreen
import com.example.github.ui.theme.GitHubTheme
import com.example.github.util.log.AppLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

                        composable(
                            route = Route.Profile.route,
                            arguments = listOf(
                                navArgument(USER_DATA) {
                                    type = NavType.ParcelableType(UserData::class.java)
                                    nullable = true
                                    defaultValue = LoginSession.userData
                                },
                            )) {

                            BackHandler(true) {
                                moveTaskToBack(true)
                            }

                            val userData = it.arguments?.get(USER_DATA) as? UserData
                            if (userData != null)
                                ProfileScreen(navController, userData)
                            else {
                                showCommonError(this@MainActivity)
                                navController.popBackStack()
                            }
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

                        composable(
                            route = Route.User.route,
                            arguments = listOf(
                                navArgument(USER_DATA) { type = NavType.ParcelableType(UserData::class.java) },
                            )) {

                            val userData = it.arguments?.get(USER_DATA) as? UserData
                            if (userData != null)
                                UserScreen(navController, userData)
                            else {
                                showCommonError(this@MainActivity)
                                navController.popBackStack()
                            }
                        }

                        composable(
                            route = Route.Search.route,
                            arguments = listOf(
                                navArgument(USER_DATA) {
                                    type = NavType.ParcelableType(UserData::class.java)
                                    nullable = true
                                    defaultValue = null
                                }
                            )) {

                            SearchScreen(it.arguments?.get(USER_DATA) as? UserData, navController)
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
            Toast.LENGTH_SHORT
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