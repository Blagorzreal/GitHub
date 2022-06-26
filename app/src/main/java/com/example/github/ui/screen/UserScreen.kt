package com.example.github.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.data.data.UserData
import com.example.github.ui.view.*
import com.example.github.vm.user.UserViewModel
import com.example.github.vm.repos.ReposViewModel
import com.example.github.vm.repos.ReposViewModelFactory
import com.example.github.vm.user.UserViewModelFactory

@Composable
fun UserScreen(
    navController: NavHostController,
    userData: UserData,
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userData)),
    reposViewModel: ReposViewModel = viewModel(factory = ReposViewModelFactory(userData))) {

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

                InfoButtons(userViewModel.data.collectAsState())

                RefreshableRepoItems(
                    showHeader = false,
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