package com.example.github.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.UserData
import com.example.github.data.remote.ResponseResult
import com.example.github.ui.navigation.Route.Companion.userScreenNavigation
import com.example.github.ui.view.*
import com.example.github.util.CommonHelper
import com.example.github.vm.FollowersViewModel
import com.example.github.vm.SearchViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun SearchScreen(
    navController: NavHostController,
    userData: UserData?,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    var hideFollowers by rememberSaveable { mutableStateOf(userData == null) }

    val followersViewModel: FollowersViewModel? = if (userData != null)
        hiltViewModel()
    else
        null

    OnStartDisposableEffect(
        owner = LocalLifecycleOwner.current,
        onStart = {
            if (!hideFollowers)
                followersViewModel?.updateFollowers()
        }
    )

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            BackTopAppBar(navController = navController, text = stringResource(R.string.search))
        },
        content = {
            Column(
                Modifier
                    .padding(it.calculateBottomPadding())
                    .fillMaxWidth()
            ) {
                CommonRow {
                    CommonTextField(
                        text = searchViewModel.searchText.collectAsState(),
                        label = stringResource(R.string.search),
                        onValueChange = searchViewModel::onSearchTextChanged,
                        onDone = {
                            hideFollowers = true
                            clearFocusAndSearch(focusManager, searchViewModel::search)
                        },
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        isDisabled = searchViewModel.isLoading.collectAsState()
                    )
                }

                if (hideFollowers) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        UserItem(
                            navController = navController,
                            usersState = searchViewModel.items.collectAsState(),
                            hasMorePagesState = searchViewModel.hasMorePages.collectAsState(),
                            isLoadingState = searchViewModel.isLoading.collectAsState(),
                            loadNextPage = searchViewModel::loadNextPage,
                            lazyListState = rememberLazyListState()
                        )
                    }
                } else if (followersViewModel != null)
                    FollowersView(navController, followersViewModel)
                else
                    hideFollowers = false
            }
        }
    )

    ResponseError(
        errorState = searchViewModel.error.collectAsState(),
        resetError = searchViewModel::resetError,
        customErrorMessage = stringResource(R.string.unable_to_search)
    )

    ValidationError(
        validationErrorState = searchViewModel.validationError.collectAsState(),
        resetValidationError = searchViewModel::resetValidationError
    )
}

@Composable
private fun FollowersView(
    navController: NavHostController,
    followersViewModel: FollowersViewModel) {

    FollowersItems(
        navController,
        followersViewModel.data.collectAsState(),
        followersViewModel.isLoading.collectAsState(),
        followersViewModel.error.collectAsState(),
        followersViewModel::resetError,
        followersViewModel::updateFollowers)
}

@Composable
private fun FollowersItems(
    navController: NavHostController,
    usersState: State<List<UserData>?>,
    isLoadingState: State<Boolean>,
    errorState: State<ResponseResult.ResponseError>,
    resetError: () -> Unit,
    followers: () -> Unit) {

    val users = usersState.value

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isLoadingState.value),
            onRefresh = followers
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 6.dp), state = rememberLazyListState()
            ) {
                if (users == null)
                    return@LazyColumn

                if (users.isNotEmpty()) {
                    items(
                        items = users,
                        key = { it.id }) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable { userScreenNavigation(navController, it) }
                        ) {
                            Box(Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                                EllipsesText(it.username)
                            }
                        }
                    }
                } else if (!isLoadingState.value) {
                    item {
                        CommonRow {
                            Text(stringResource(R.string.no_followers_found))
                        }
                    }
                }
            }
        }
    }

    ResponseError(
        errorState = errorState,
        resetError = resetError,
        customErrorMessage = stringResource(R.string.unable_to_fetch_followers))
}

private fun clearFocusAndSearch(focusManager: FocusManager, search: () -> Unit) {
    focusManager.clearFocus()
    search()
}

@Composable
private fun UserItem(
    navController: NavHostController,
    usersState: State<List<UserData>?>,
    isLoadingState: State<Boolean>,
    hasMorePagesState: State<Boolean>,
    loadNextPage: () -> Unit,
    lazyListState: LazyListState
) {
    if (isLoadingState.value)
        CircularProgressIndicator()

    val users = usersState.value ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 6.dp), state = lazyListState
    ) {
        if (users.isNotEmpty()) {
            items(
                items = users,
                key = { it.id }) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { userScreenNavigation(navController, it) }
                ) {
                    Box(Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                        EllipsesText(it.username)
                    }
                }
            }

            if (hasMorePagesState.value) {
                item {
                    CommonRow {
                        Button(onClick = loadNextPage, enabled = !isLoadingState.value) {
                            Text(stringResource(R.string.more))
                        }
                    }
                }
            }
        } else if (!isLoadingState.value) {
            item {
                CommonRow {
                    Text(stringResource(R.string.no_results_found))
                }
            }
        }
    }
}

@Composable
private fun CommonRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        content()
    }
}

@Composable
private fun ValidationError(
    validationErrorState: State<Boolean>,
    resetValidationError: () -> Unit) {

    if (validationErrorState.value) {
        resetValidationError()
        CommonHelper.showToast(LocalContext.current, stringResource(R.string.invalid_username_message))
    }
}