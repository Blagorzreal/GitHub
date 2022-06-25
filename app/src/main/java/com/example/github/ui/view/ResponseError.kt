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
fun ResponseError(responseError: State<ResponseResult.ResponseError?>) {
    val error = responseError.value ?: return

    val context = LocalContext.current

    val responseErrorMessage = when {
        ErrorHelper.isUsernameNotFoundError(error) -> stringResource(R.string.username_not_found_error)
        ErrorHelper.isForbiddenError(error) -> stringResource(R.string.forbidden_error)
        else -> stringResource(R.string.common_error)
    }

    Toast.makeText(context, responseErrorMessage, Toast.LENGTH_LONG).show()
}