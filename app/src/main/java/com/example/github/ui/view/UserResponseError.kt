package com.example.github.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.github.R
import com.example.github.vm.UserViewModel

@Composable
fun UserResponseError(userViewModel: UserViewModel) {
    ResponseError(
        errorState = userViewModel.error.collectAsState(),
        resetError = userViewModel::resetError,
        customErrorMessage = stringResource(R.string.unable_to_fetch_user)
    )
}