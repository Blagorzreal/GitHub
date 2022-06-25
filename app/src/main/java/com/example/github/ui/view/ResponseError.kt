package com.example.github.ui.view

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.github.R
import com.example.github.data.remote.ResponseResult
import com.example.github.util.ErrorHelper

@Composable
fun ResponseError(responseErrorState: State<ResponseResult.ResponseError?>) {
    val responseError = responseErrorState.value ?: return

    val responseErrorMessage = when {
        ErrorHelper.isUsernameNotFoundError(responseError) -> stringResource(R.string.username_not_found_error)
        ErrorHelper.isForbiddenError(responseError) -> stringResource(R.string.forbidden_error)
        else -> stringResource(R.string.common_error)
    }

    Toast.makeText(LocalContext.current, responseErrorMessage, Toast.LENGTH_LONG).show()
}