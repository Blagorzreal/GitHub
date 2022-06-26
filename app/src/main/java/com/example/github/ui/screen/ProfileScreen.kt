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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.ui.view.*
import com.example.github.vm.ProfileViewModel
import com.example.github.vm.repos.starred.StarredReposViewModel
import com.example.github.vm.repos.starred.StarredViewModelFactory
import com.example.github.vm.user.UserViewModel
import com.example.github.vm.user.UserViewModelFactory

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userData: UserData,
    profileViewModel: ProfileViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userData)),
    starredReposViewModel: StarredReposViewModel = viewModel(factory = StarredViewModelFactory(userData))) {

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
                title = { Text( text = userData.username) },
                actions = {
                    IconButton(onClick = { showPopup = true }) {
                        AsyncImage(
                            modifier = Modifier.padding(4.dp),
                            model = userData.avatarUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            placeholder = painterResource(R.drawable.ic_avatar),
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

            InfoButtons(userDataState = userViewModel.data.collectAsState())

            if (tabIndex == 0) {
                RefreshableRepoItems(
                    lazyListState = ownLazyListState,
                    reposState = starredReposViewModel.data.collectAsState(),
                    isLoadingState = starredReposViewModel.isLoading.collectAsState(),
                    onClick = { repoData ->
                        navigateToRepositoryScreen(navController, repoData)
                    },
                    refresh = starredReposViewModel::updateRepos
                )
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
        isLoggedOutState = profileViewModel.isLoggedOut.collectAsState())
}

@Composable
private fun HandleLogout(navController: NavHostController, isLoggedOutState: State<Boolean>) {
    if (isLoggedOutState.value)
        InclusiveNavigation(navController, Route.Login, Route.Profile)
}