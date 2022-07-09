package com.example.github

import android.content.Context
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
import androidx.navigation.NavHostController
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
import com.example.github.ui.screen.*
import com.example.github.ui.screen.LoginScreen
import com.example.github.ui.theme.GitHubTheme
import com.example.github.util.CommonHelper
import com.example.github.util.log.AppLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    companion object {
        private const val TAG = "Main activity"

        private fun userDataNavArgument(defaultUserData: UserData? = null) =
            listOf(
                navArgument(USER_DATA) {
                    type = NavType.ParcelableType(UserData::class.java)
                    nullable = true
                    defaultValue = defaultUserData
                })

        private fun showCommonErrorAndPopBack(navController: NavHostController, context: Context) {
            CommonHelper.showToast(context, context.getString(R.string.common_error))
            navController.popBackStack()
        }
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
                            arguments = userDataNavArgument(LoginSession.userData)) {

                            BackHandler(true) {
                                moveTaskToBack(true)
                            }

                            ProfileScreen(navController)
                        }

                        composable(
                            route = Route.Repository.route,
                            arguments = listOf(
                                navArgument(REPO_DATA) {
                                    type = NavType.ParcelableType(RepoData::class.java)
                                    nullable = true
                                },
                            )) {

                            val repoData = it.arguments?.get(REPO_DATA) as? RepoData
                            if (repoData != null)
                                RepositoryScreen(navController)
                            else
                                showCommonErrorAndPopBack(navController, this@MainActivity)
                        }

                        composable(
                            route = Route.User.route,
                            arguments = userDataNavArgument()) {

                            val userData = it.arguments?.get(USER_DATA) as? UserData
                            if (userData != null)
                                UserScreen(navController)
                            else
                                showCommonErrorAndPopBack(navController, this@MainActivity)
                        }

                        composable(
                            route = Route.Search.route,
                            arguments = userDataNavArgument()) {

                            SearchScreen(navController, it.arguments?.get(USER_DATA) as? UserData)
                        }
                    }
                }
            }
        }
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