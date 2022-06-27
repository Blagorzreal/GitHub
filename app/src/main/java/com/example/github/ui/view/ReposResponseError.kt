package com.example.github.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.github.R
import com.example.github.vm.ReposViewModel

@Composable
fun ReposResponseError(reposViewModel: ReposViewModel) {
    ResponseError(
        errorState = reposViewModel.error.collectAsState(),
        resetError = reposViewModel::resetError,
        customErrorMessage = stringResource(R.string.unable_to_fetch_repos)
    )
}