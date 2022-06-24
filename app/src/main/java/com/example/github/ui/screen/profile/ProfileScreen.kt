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

    var showPopup by rememberSaveable { mutableStateOf(false) }

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
        OwnRepoItems(
            Modifier.padding(bottom = it.calculateBottomPadding()),
            navController,
            rememberLazyListState(),
            profileViewModel.data.collectAsState(),
            profileViewModel.isLoading.collectAsState(),
            profileViewModel::updateRepos)
    }

    HandleLogout(navController, profileViewModel.isLoggedOut.collectAsState())
}

@Composable
fun OwnRepoItems(
    modifier: Modifier,
    navController: NavHostController,
    lazyListState: LazyListState,
    reposState: State<List<RepoData>?>,
    isLoadingState: State<Boolean>,
    refresh: () -> Unit) {

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoadingState.value),
        onRefresh = refresh
    ) {
        val repos = reposState.value
        LazyColumn(modifier = modifier.then(Modifier.fillMaxSize()), state = lazyListState) {
            if (!repos.isNullOrEmpty()) {
                item {
                    ItemsHeader(text = stringResource(R.string.own_repos))
                }

                items(repos) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable {

                            }
                    ) {
                        Box(Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                            EllipsesText(it.name)
                        }
                    }
                }
            } else if (!isLoadingState.value) {
                item {
                    if (repos != null)
                        ItemsHeader(stringResource(R.string.no_own_repos_available))
                }
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