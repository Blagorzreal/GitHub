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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.RepoData
import com.example.github.ui.navigation.Route.Companion.userScreenNavigation
import com.example.github.ui.view.BackTopAppBar
import com.example.github.ui.view.Star
import com.example.github.vm.RepoViewModel

@Composable
fun RepositoryScreen(
    navController: NavHostController,
    repoData: RepoData,
    repoViewModel: RepoViewModel = hiltViewModel()) {

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
                        onClick = { userScreenNavigation(navController, repoData.owner) },
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(text = repoData.owner.username)
                    }
                }

                Star(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(50.dp),
                    selected = repoViewModel.starred.collectAsState().value,
                    onClick = repoViewModel::updateStarred)
            }
        }
    )

    HandleError(
        id = repoData.id,
        errorState = repoViewModel.error.collectAsState(),
        resetError = repoViewModel::resetError)
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