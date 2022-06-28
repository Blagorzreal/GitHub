package com.example.github.ui.view

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CommonTextField(
    text: State<String>,
    label: String,
    onValueChange: (value: String) -> Unit,
    onDone: (() -> Unit)?,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    isDisabled: State<Boolean>
) {

    TextField(
        label = { Text(text = label) },
        value = text.value,
        onValueChange = onValueChange,
        maxLines = 1,
        enabled = !isDisabled.value,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(onDone = { onDone?.invoke() })
    )
}