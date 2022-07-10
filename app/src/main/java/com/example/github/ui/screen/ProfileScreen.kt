package com.example.github.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.ui.navigation.Route
import com.example.github.ui.view.*
import com.example.github.util.CommonHelper
import com.example.github.util.Constants
import com.example.github.vm.profile.ProfileViewModel
import com.example.github.vm.StarredReposViewModel
import com.example.github.vm.UserViewModel
import com.example.github.vm.profile.LogoutState

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    starredReposViewModel: StarredReposViewModel = hiltViewModel()) {

    OnStartDisposableEffect(
        owner = LocalLifecycleOwner.current,
        onStart = {
            userViewModel.updateUser()
            starredReposViewModel.updateRepos()
        }
    )

    val ownLazyListState = rememberLazyListState()
    val starredLazyListState = rememberLazyListState()

    var showPopup by rememberSaveable { mutableStateOf(false) }

    var tabIndex by rememberSaveable { mutableStateOf(0) }
    val tabData = listOf(
        stringResource(R.string.own),
        stringResource(R.string.starred)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text( text = profileViewModel.userData?.username ?: Constants.EMPTY_STRING) },
                actions = {
                    IconButton(onClick = { showPopup = true }) {
                        AsyncImage(
                            modifier = Modifier.padding(4.dp),
                            model = profileViewModel.userData?.avatarUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            error = painterResource(R.drawable.ic_avatar)
                        )

                        DropdownMenu(
                            expanded = showPopup,
                            onDismissRequest = { showPopup = false }) {
                            DropdownMenuItem(onClick = profileViewModel::logOut) {
                                Text(text = stringResource(R.string.logout))
                            }
                        }
                    }
                    IconButton(
                        onClick = { navController.navigate(Route.Search.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .padding(bottom = it.calculateBottomPadding())
            .fillMaxSize()) {

            TabRow(selectedTabIndex = tabIndex) {
                tabData.forEachIndexed { index, text ->
                    Tab(selected = tabIndex == index, onClick = {
                        tabIndex = index
                    }, text = {
                        Text(text = text)
                    })
                }
            }

            InfoButtons(navController, userViewModel)

            if (tabIndex == 0) {
                RefreshableRepoItems(
                    headerText = stringResource(R.string.own_repos),
                    noItemsText = stringResource(R.string.no_own_repos_available),
                    lazyListState = ownLazyListState,
                    reposState = starredReposViewModel.data.collectAsState(),
                    isLoadingState = starredReposViewModel.isLoading.collectAsState(),
                    onClick = { repoData ->
                        navigateToRepositoryScreen(navController, repoData)
                    },
                    refresh = {
                        userViewModel.updateUser()
                        starredReposViewModel.updateRepos()
                    })
            } else {
                RepoItems(
                    headerText = stringResource(R.string.starred_repos),
                    noItemsText = stringResource(R.string.no_starred_repos_available),
                    reposState = starredReposViewModel.starredRepos.collectAsState(),
                    lazyListState = starredLazyListState,
                    onClick = { repoData ->
                        navigateToRepositoryScreen(navController, repoData)
                    },
                    isLoadingState = null
                )
            }
        }
    }

    UserResponseError(userViewModel = userViewModel)

    ReposResponseError(reposViewModel = starredReposViewModel)

    HandleLogout(
        navController = navController,
        logoutStateState = profileViewModel.logoutState.collectAsState())
}

@Composable
private fun HandleLogout(
    navController: NavHostController,
    logoutStateState: State<LogoutState>) {

    val logoutState = logoutStateState.value
    if (logoutState !is LogoutState.NotLoggedOut) {

        if (logoutState is LogoutState.LoggedOutWithError) {
            if (!logoutState.handled) {
                logoutState.handled = true
                CommonHelper.showToast(LocalContext.current, stringResource(R.string.logout_with_error))
            }
        }

        LaunchedEffect(true) {
            Route.inclusiveNavigation(navController, Route.Login, Route.Profile)
        }
    }
}