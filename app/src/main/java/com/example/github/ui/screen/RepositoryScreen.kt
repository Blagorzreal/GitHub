package com.example.github.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.ui.view.BackTopAppBar
import com.example.github.vm.repo.RepoViewModel
import com.example.github.vm.repo.RepoViewModelFactory

@Composable
fun RepositoryScreen(
    navController: NavHostController,
    repoData: RepoData,
    repoViewModel: RepoViewModel = viewModel(factory = RepoViewModelFactory(repoData))) {

    Scaffold(
        topBar = {
           BackTopAppBar(navController = navController, text = repoData.name)
        },
        content = {
            Column(
                Modifier
                    .padding(it.calculateBottomPadding())
                    .fillMaxWidth()) {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { navigateToUserScreen(navController, repoData.owner) },
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(text = repoData.owner.username)
                    }
                }

                Star(
                    repoDataState = repoViewModel.repoData.collectAsState(),
                    onClick = repoViewModel::updateStarred)

                HandleError(
                    id = repoData.id,
                    errorState = repoViewModel.error.collectAsState(),
                    resetError = repoViewModel::resetError)
            }
        }
    )
}

private fun navigateToUserScreen(navController: NavHostController, userData: UserData) {
    navController.navigate(Route.User.route)
    navController.currentBackStackEntry?.savedStateHandle?.set(Route.USER_DATA, userData)
}

@Composable
private fun Star(repoDataState: State<RepoData>, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            painter = painterResource(R.drawable.star),
            tint = if (repoDataState.value.starred) Color.Yellow else Color.DarkGray,
            contentDescription = null
        )
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