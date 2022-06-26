package com.example.github.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.data.data.UserData
import com.example.github.ui.view.RefreshableRepoItems
import com.example.github.ui.view.navigateToRepositoryScreen
import com.example.github.vm.user.UserViewModel
import com.example.github.vm.user.UserViewModelFactory

@Composable
fun UserScreen(
    navController: NavHostController,
    userData: UserData,
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userData))) {

    val ownLazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = userData.username) },
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                } else null

            )
        },
        content = {
            Column(
                Modifier
                    .padding(it.calculateBottomPadding())
                    .fillMaxWidth()) {

                RefreshableRepoItems(
                    lazyListState = ownLazyListState,
                    reposState = userViewModel.data.collectAsState(),
                    isLoadingState = userViewModel.isLoading.collectAsState(),
                    onClick = { repoData ->
                        navigateToRepositoryScreen(navController, repoData)
                    },
                    refresh = userViewModel::updateRepos
                )
            }
        }
    )
}