package com.example.github.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.ui.navigation.Route
import com.example.github.util.CommonHelper
import com.example.github.vm.UserViewModel

@Composable
fun InfoButtons(navController: NavHostController, userViewModel: UserViewModel) {
    InfoButton(R.string.followers, userViewModel.followers.collectAsState()) {
        navController.navigate(Route.Search.route)
        navController.currentBackStackEntry?.arguments?.putParcelable(Route.USER_DATA, userViewModel.userData)
    }

    val context = LocalContext.current

    InfoButton(R.string.following, userViewModel.following.collectAsState()) {
        CommonHelper.showToast(context, context.getString(R.string.following_not_implemented))
    }
}

@Composable
private fun InfoButton(descriptionResource: Int, valueState: State<Long?>, onClick: () -> Unit) {
    val value = valueState.value ?: return

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