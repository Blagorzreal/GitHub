package com.example.github.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.github.R
import com.example.github.data.remote.ResponseResult
import com.example.github.ui.theme.Typography
import com.example.github.ui.view.CommonSpacer
import com.example.github.util.LoginHelper
import com.example.github.vm.LoginViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.width(80.dp).height(80.dp),
            painter = painterResource(R.drawable.ic_git_hub),
            contentDescription = stringResource(R.string.git_hub_logo)
        )

        Text(
            text = stringResource(R.string.app_name),
            style = Typography.h1,
            fontWeight = FontWeight.Bold
        )

        CommonSpacer()

        LoginComponentsWithLoading(focusManager, loginViewModel, loginViewModel.isLoading.collectAsState())

        HandleResponseError(loginViewModel.responseError.collectAsState())
    }
}

private fun clearFocusAndLogin(focusManager: FocusManager, login: () -> Unit) {
    focusManager.clearFocus()
    login()
}

@Composable
private fun LoginComponentsWithLoading(focusManager: FocusManager, loginViewModel: LoginViewModel, isLoading: State<Boolean>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading.value)
            CircularProgressIndicator()
        else {
            LoginTextField(
                text = loginViewModel.username.collectAsState(),
                label = stringResource(R.string.username),
                onValueChange = loginViewModel::onUsernameChanged,
                onDone = { clearFocusAndLogin(focusManager, loginViewModel::login) },
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )

            HandleUsernameValidationError(loginViewModel.validationError.collectAsState())

            Spacer(modifier = Modifier.height(8.dp))

            CommonSpacer()

            Button(
                onClick = { clearFocusAndLogin(focusManager, loginViewModel::login) },
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.login),
                    modifier = Modifier.padding(80.dp, 6.dp, 80.dp, 6.dp)
                )
            }
        }
    }
}

@Composable
private fun LoginTextField(
    text: State<String>,
    label: String,
    onValueChange: (value: String) -> Unit,
    onDone: (() -> Unit)?,
    imeAction: ImeAction,
    keyboardType: KeyboardType) {

    TextField(
        label = { Text(text = label) },
        value = text.value,
        onValueChange = onValueChange,
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(onDone = { onDone?.invoke() })
    )
}

@Composable
private fun HandleUsernameValidationError(usernameValidationError: State<UsernameValidationError?>) {
    val validationErrorMessage = when (usernameValidationError.value) {
        is UsernameValidationError.EmptyUsernameValidationError ->
            stringResource(R.string.empty_username_message)
        is UsernameValidationError.BadUsernameValidationError ->
            stringResource(R.string.invalid_username_message)
        else -> return
    }

    Text(
        text = validationErrorMessage,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption.plus(TextStyle(fontSize = 16.sp)),
        modifier = Modifier.padding(start = 16.dp))
}

@Composable
fun HandleResponseError(responseError: State<ResponseResult.ResponseError?>) {
    val error = responseError.value ?: return

    val context = LocalContext.current

    val responseErrorMessage =
        if (LoginHelper.isUsernameNotFoundError(error))
            stringResource(R.string.username_not_found_error)
        else
            stringResource(R.string.common_error)

    Toast.makeText(context, responseErrorMessage, Toast.LENGTH_LONG).show()
}