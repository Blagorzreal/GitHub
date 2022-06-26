package com.example.github.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.UserData
import com.example.github.ui.view.RefreshableRepoItems
import com.example.github.ui.view.ResponseError
import com.example.github.ui.view.navigateToRepositoryScreen
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

                InfoButtons(userViewModel.data.collectAsState())

                RefreshableRepoItems(
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

    ResponseError(
        errorState = userViewModel.error.collectAsState(),
        resetError = userViewModel::resetError)

    ResponseError(
        errorState = reposViewModel.error.collectAsState(),
        resetError = reposViewModel::resetError)
}

@Composable
private fun InfoButtons(userDataState: State<UserData?>) {
    val userData = userDataState.value ?: return
    InfoButton(R.string.followers, userData.followers)
    InfoButton(R.string.following, userData.following)
}

@Composable
private fun InfoButton(descriptionResource: Int, value: Long?) {
    if (value == null)
        return

    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
        ) {
            Text(text = stringResource(descriptionResource, value))
        }
    }
}

@Composable
private fun HandleError(id: Long, errorState: State<Boolean>, resetError: () -> Unit) {
    val error = errorState.value
    if (error) {
        resetError()

        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.starred_update_error, id),
            Toast.LENGTH_SHORT
        ).show()
    }
}