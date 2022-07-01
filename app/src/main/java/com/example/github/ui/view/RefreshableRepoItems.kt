package com.example.github.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.github.data.data.RepoData
import com.example.github.ui.navigation.Route
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun RefreshableRepoItems(
    headerText: String,
    noItemsText: String,
    lazyListState: LazyListState,
    reposState: State<Collection<RepoData>?>,
    isLoadingState: State<Boolean>,
    onClick: (repo: RepoData) -> Unit,
    refresh: () -> Unit) {

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoadingState.value),
        onRefresh = refresh
    ) {
        RepoItems(
            headerText = headerText,
            noItemsText = noItemsText,
            reposState = reposState,
            lazyListState = lazyListState,
            onClick = onClick,
            isLoadingState = isLoadingState)
    }
}

@Composable
fun RepoItems(
    headerText: String,
    noItemsText: String,
    reposState: State<Collection<RepoData>?>,
    lazyListState: LazyListState,
    onClick: (repo: RepoData) -> Unit,
    isLoadingState: State<Boolean>?
) {
    val repos = reposState.value
    val isLoadingStateNotAvailable = isLoadingState == null

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 6.dp), state = lazyListState) {
        if (!repos.isNullOrEmpty()) {
            item {
                ItemsHeader(text = headerText)
            }

            items(repos.toList()) {
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
        } else if ((isLoadingStateNotAvailable || !isLoadingState!!.value) && (repos != null)) {
            item {
                ItemsHeader(noItemsText)
            }
        }
    }
}

@Composable
private fun ItemsHeader(text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)
        CommonSpacer()
    }
}

fun navigateToRepositoryScreen(navController: NavHostController, repoData: RepoData) {
    navController.navigate(Route.Repository.route)
    navController.currentBackStackEntry?.savedStateHandle?.set(Route.REPO_DATA, repoData)
}