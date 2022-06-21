package com.example.github.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.github.R
import com.example.github.ui.theme.Typography
import com.example.github.ui.view.CommonSpacer
import com.example.github.util.Constants

@Composable
fun LoginScreen() {
    val focusManager = LocalFocusManager.current

    var username by rememberSaveable { mutableStateOf(Constants.EMPTY_STRING) }
    var password by rememberSaveable { mutableStateOf(Constants.EMPTY_STRING) }

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

        LoginTextField(
            label = stringResource(R.string.username),
            value = username,
            onValueChange = { username = it },
            onDone = { },
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            visualTransformation = VisualTransformation.None)

        Spacer(modifier = Modifier.height(8.dp))

        LoginTextField(
            label = stringResource(R.string.password),
            value = password,
            onValueChange = { password = it },
            onDone = null,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation())

        CommonSpacer()

        Button(
            onClick = { },
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(
                text = stringResource(R.string.login),
                modifier = Modifier.padding(80.dp, 6.dp, 80.dp, 6.dp)
            )
        }
    }
}

@Composable
private fun LoginTextField(
    label: String,
    value: String,
    onValueChange: (value: String) -> Unit,
    onDone: (() -> Unit)?,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation) {

    TextField(
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChange,
        maxLines = 1,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(onDone = { onDone?.invoke() })
    )
}