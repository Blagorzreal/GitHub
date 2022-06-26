package com.example.github.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.RepoData
import com.example.github.vm.repository.RepositoryViewModel
import com.example.github.vm.repository.RepositoryViewModelFactory

@Composable
fun RepositoryScreen(
    navController: NavHostController,
    repoData: RepoData,
    repositoryViewModel: RepositoryViewModel = viewModel(factory = RepositoryViewModelFactory(repoData))) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = repoData.name) },
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
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(text = repoData.owner.username)
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
                    ) {
                        Text(text = "Contributors")
                    }
                }

                Star(
                    repoDataState = repositoryViewModel.repoData.collectAsState(),
                    onClick = repositoryViewModel::updateStarred)

                HandleError(
                    id = repoData.id,
                    errorState = repositoryViewModel.error.collectAsState(),
                    resetError = repositoryViewModel::resetError)
            }
        }
    )
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