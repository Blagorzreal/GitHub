package com.example.github.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.github.R
import com.example.github.data.remote.ResponseResult
import com.example.github.util.CommonHelper
import com.example.github.util.ErrorHelper

@Composable
fun ResponseError(
    errorState: State<ResponseResult.ResponseError>,
    resetError: () -> Unit,
    customErrorMessage: String = stringResource(R.string.common_error)) {

    val error = errorState.value
    if (error is ResponseResult.None)
        return

    resetError()

    val responseErrorMessage = when {
        ErrorHelper.isUsernameNotFoundError(error) -> stringResource(R.string.not_found_error)
        ErrorHelper.isForbiddenError(error) -> stringResource(R.string.forbidden_error)
        ErrorHelper.isNoInternetConnectionError(error) -> stringResource(R.string.no_internet_connection_error)
        else -> customErrorMessage
    }

    CommonHelper.showToast(LocalContext.current, responseErrorMessage)
}