package com.example.github.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route

@Composable
fun InfoButtons(navController: NavHostController, userDataState: State<UserData?>) {
    val userData = userDataState.value ?: return
    InfoButton(R.string.followers, userData.followers) {
        navController.navigate(Route.Search.route)
        navController.currentBackStackEntry?.savedStateHandle?.set(Route.USER_DATA, userData)
    }

    InfoButton(R.string.following, userData.following) { }
}

@Composable
private fun InfoButton(descriptionResource: Int, value: Long?, onClick: () -> Unit) {
    if (value == null)
        return

    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
        ) {
            Text(text = stringResource(descriptionResource, value))
        }
    }
}