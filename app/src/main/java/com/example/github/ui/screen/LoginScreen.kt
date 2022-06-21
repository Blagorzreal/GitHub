package com.example.github.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.github.R
import com.example.github.ui.theme.Typography
import com.example.github.ui.view.CommonSpacer
import com.example.github.util.Constants

@Composable
fun LoginScreen() {
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

        LoginTextField(
            label = stringResource(R.string.username),
            onValueChange = { },
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            visualTransformation = VisualTransformation.None)

        Spacer(modifier = Modifier.height(8.dp))

        LoginTextField(
            label = stringResource(R.string.password),
            onValueChange = { },
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
    onValueChange: (value: String) -> Unit,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation) {

    TextField(
        label = { Text(text = label) },
        value = Constants.EMPTY_STRING,
        onValueChange = onValueChange,
        maxLines = 1,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
        )
    )
}