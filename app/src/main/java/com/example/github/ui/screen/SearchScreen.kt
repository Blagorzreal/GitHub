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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.SearchData
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.ui.view.*
import com.example.github.vm.FollowersViewModel
import com.example.github.vm.SearchViewModel
import com.example.github.vm.factory.UserDataViewModelFactory
import com.example.github.vm.factory.ViewModelType

@Composable
fun SearchScreen(
    userData: UserData?,
    navController: NavHostController,
    searchViewModel: SearchViewModel = viewModel()) {

    var followersViewModel: FollowersViewModel? = null
    if (userData != null)
        followersViewModel = viewModel(factory = UserDataViewModelFactory(userData, ViewModelType.Followers))

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
                        onDone = { clearFocusAndSearch(focusManager, searchViewModel::search) },
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        isDisabled = searchViewModel.isLoading.collectAsState()
                    )
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    UserItem(
                        navController = navController,
                        usersState = searchViewModel.data.collectAsState(),
                        hasNextPageState = searchViewModel.hasNextPage.collectAsState(),
                        isLoadingState = searchViewModel.isLoading.collectAsState(),
                        searchNextPage = searchViewModel::searchNextPage,
                        lazyListState = rememberLazyListState()
                    )
                }
            }
        }
    )

    ResponseError(
        errorState = searchViewModel.error.collectAsState(),
        resetError = searchViewModel::resetError,
        customErrorMessage = stringResource(R.string.unable_to_search)
    )
}

private fun clearFocusAndSearch(focusManager: FocusManager, search: () -> Unit) {
    focusManager.clearFocus()
    search()
}

@Composable
private fun UserItem(
    navController: NavHostController,
    usersState: State<SearchData?>,
    isLoadingState: State<Boolean>,
    hasNextPageState: State<Boolean>,
    searchNextPage: () -> Unit,
    lazyListState: LazyListState
) {
    if (isLoadingState.value)
        CircularProgressIndicator()

    val users = usersState.value?.items

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 6.dp), state = lazyListState
    ) {
        if (users == null)
            return@LazyColumn

        if (users.isNotEmpty()) {
            items(users) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Route.User.route)
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                Route.USER_DATA,
                                it
                            )
                        }
                ) {
                    Box(Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                        EllipsesText(it.username)
                    }
                }
            }

            if (hasNextPageState.value) {
                item {
                    CommonRow {
                        Button(onClick = searchNextPage, enabled = !isLoadingState.value) {
                            Text(stringResource(R.string.next))
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