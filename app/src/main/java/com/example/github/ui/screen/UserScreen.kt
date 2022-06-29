package com.example.github.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.UserData
import com.example.github.ui.view.*
import com.example.github.vm.factory.UserDataViewModelFactory
import com.example.github.vm.factory.ViewModelType
import com.example.github.vm.UserViewModel
import com.example.github.vm.ReposViewModel

@Composable
fun UserScreen(
    navController: NavHostController,
    userData: UserData,
    userViewModel: UserViewModel = viewModel(factory = UserDataViewModelFactory(userData, ViewModelType.User)),
    reposViewModel: ReposViewModel = viewModel(factory = UserDataViewModelFactory(userData, ViewModelType.Repos))) {

    val ownLazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            BackTopAppBar(navController = navController, text = userData.username)
        },
        content = {
            Column(
                Modifier
                    .padding(it.calculateBottomPadding())
                    .fillMaxWidth()) {

                InfoButtons(navController, userViewModel.data.collectAsState())

                RefreshableRepoItems(
                    headerText = stringResource(R.string.repos),
                    noItemsText = stringResource(R.string.no_repos_available),
                    lazyListState = ownLazyListState,
                    reposState = reposViewModel.data.collectAsState(),
                    isLoadingState = reposViewModel.isLoading.collectAsState(),
                    onClick = { repoData ->
                        navigateToRepositoryScreen(navController, repoData)
                    },
                    refresh = {
                        userViewModel.updateUser()
                        reposViewModel.updateRepos()
                    }
                )
            }
        }
    )

    UserResponseError(userViewModel = userViewModel)

    ReposResponseError(reposViewModel = reposViewModel)
}