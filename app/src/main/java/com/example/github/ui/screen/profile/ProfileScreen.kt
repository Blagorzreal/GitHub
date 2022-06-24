package com.example.github.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.github.vm.profile.ProfileViewModel
import com.example.github.vm.profile.ProfileViewModelFactory

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userData: UserData,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userData))) {

    var showPopup by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text( text = userData.username) },
                actions = {
                    IconButton(onClick = { showPopup = true }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null
                        )

                        DropdownMenu(
                            expanded = showPopup,
                            onDismissRequest = { showPopup = false }) {
                            DropdownMenuItem(onClick = profileViewModel::logOut) {
                                Text(text = stringResource(R.string.logout))
                            }
                        }
                    }
                    IconButton(onClick = { },) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        RepoItems(
            Modifier.padding(bottom = it.calculateBottomPadding()),
            navController,
            profileViewModel.data.collectAsState(),
            rememberLazyListState())
    }

    HandleLogout(navController, profileViewModel.isLoggedOut.collectAsState())
}

@Composable
fun RepoItems(
    modifier: Modifier,
    navController: NavHostController,
    reposState: State<List<RepoData>?>,
    lazyListState: LazyListState) {

    val repos = reposState.value
    if (!repos.isNullOrEmpty()) {
        LazyColumn(modifier = modifier.then(Modifier.fillMaxSize()), state = lazyListState) {
            item {
                Text(text = stringResource(R.string.own_repos))
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
        LazyColumn(modifier = modifier.then(Modifier.fillMaxSize())) {
            if (repos != null) {
                item {
                    Text(text = stringResource(R.string.no_own_repos_available))
                    CommonSpacer()
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