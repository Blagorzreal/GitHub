package com.example.github.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.ui.view.CommonSpacer
import com.example.github.ui.view.EllipsesText
import com.example.github.ui.view.InclusiveNavigation
import com.example.github.vm.profile.ProfileViewModel
import com.example.github.vm.profile.ProfileViewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userData: UserData,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userData))) {

    val ownLazyListState = rememberLazyListState()
    val starredLazyListState = rememberLazyListState()

    var showPopup by rememberSaveable { mutableStateOf(false) }

    var tabIndex by remember { mutableStateOf(0) }
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

            if (tabIndex == 0) {
                RefreshableRepoItems(
                    navController = navController,
                    lazyListState = ownLazyListState,
                    reposState = profileViewModel.data.collectAsState(),
                    isLoadingState = profileViewModel.isLoading.collectAsState(),
                    onClick = { profileViewModel.updateStarred(it, !it.starred) },
                    refresh = profileViewModel::updateRepos
                )
            } else {
                RepoItems(
                    headerText = stringResource(R.string.starred_repos),
                    noItemsText = stringResource(R.string.no_starred_repos_available),
                    reposState = profileViewModel.starredRepos.collectAsState(),
                    lazyListState = starredLazyListState,
                    onClick = {
                        profileViewModel.updateStarred(it, !it.starred)
                    },
                    isLoadingState = null
                )
            }
        }
    }

    HandleLogout(navController, profileViewModel.isLoggedOut.collectAsState())
}

@Composable
private fun RefreshableRepoItems(
    navController: NavHostController,
    lazyListState: LazyListState,
    reposState: State<List<RepoData>?>,
    isLoadingState: State<Boolean>,
    onClick: (repo: RepoData) -> Unit,
    refresh: () -> Unit) {

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoadingState.value),
        onRefresh = refresh
    ) {
        RepoItems(
            headerText = stringResource(R.string.own_repos),
            noItemsText = stringResource(R.string.no_own_repos_available),
            reposState = reposState,
            lazyListState = lazyListState,
            onClick = onClick,
            isLoadingState = isLoadingState)
    }
}

@Composable
private fun RepoItems(
    headerText: String,
    noItemsText: String,
    reposState: State<List<RepoData>?>,
    lazyListState: LazyListState,
    onClick: (repo: RepoData) -> Unit,
    isLoadingState: State<Boolean>?
) {
    val repos = reposState.value
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), state = lazyListState) {
        if (!repos.isNullOrEmpty()) {
            item {
                ItemsHeader(text = headerText)
            }

            items(repos) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onClick(it) }
                ) {
                    Box(Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                        EllipsesText(it.name)
                    }
                }
            }
        } else if ((isLoadingState == null) || !isLoadingState.value) {
            item {
                if ((isLoadingState == null) || (repos != null))
                    ItemsHeader(noItemsText)
            }
        }
    }
}

@Composable
private fun ItemsHeader(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)
        CommonSpacer()
    }
}

@Composable
private fun HandleLogout(navController: NavHostController, isLoggedOut: State<Boolean>) {
    if (isLoggedOut.value)
        InclusiveNavigation(navController, Route.Login, Route.Profile)
}