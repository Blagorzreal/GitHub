package com.example.github.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.ui.view.CommonSpacer
import com.example.github.ui.view.EllipsesText
import com.example.github.ui.view.InclusiveNavigation
import com.example.github.vm.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userData: UserData,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userData))) {

    val lazyListState = rememberLazyListState()

    RepoItems(navController, profileViewModel.data.collectAsState(), lazyListState)

    HandleLogout(navController, profileViewModel.isLoggedOut.collectAsState())
}

@Composable
fun RepoItems(navController: NavHostController, reposState: State<List<RepoData>?>, lazyListState: LazyListState) {
    val repos = reposState.value
    if (!repos.isNullOrEmpty()) {
        LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState) {
            item {
                Text("Own")
                CommonSpacer()
            }

            items(repos) {
                Column(
                    Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxWidth()
                        .clickable {

                        }
                ) {
                    EllipsesText(it.name)
                }
            }
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(1) {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    if (repos != null)
                        Text(text = stringResource(R.string.no_repos_available))
                }
            }
        }
    }
}

@Composable
private fun HandleLogout(navController: NavHostController, isLoggedOut: State<Boolean>) {
    if (isLoggedOut.value)
        InclusiveNavigation(navController, Route.Login, Route.Profile)
}