package com.example.github.ui.view

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.github.R

private val SELECTED_COLOR = Color.Yellow
private val UNSELECTED_COLOR = Color.DarkGray

@Composable
fun Star(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)?,
    selected: Boolean,
) {
    if (onClick != null) {
        IconButton(onClick = onClick) {
            StarIcon(modifier, selected)
        }
    } else
        StarIcon(modifier, selected)
}

@Composable
private fun StarIcon(modifier: Modifier, selected: Boolean) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.star),
        tint = if (selected) SELECTED_COLOR else UNSELECTED_COLOR,
        contentDescription = null
    )
}