package com.example.github.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.github.R

@Composable
fun RepositoryScreen(navController: NavHostController, owner: String, repo: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = repo) },
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                } else null

            )
        },
        content = {
            Column(
                Modifier
                    .padding(it.calculateBottomPadding())
                    .fillMaxWidth()) {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {  },
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(text = owner)
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {  },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
                    ) {
                        Text(text = "Contributors")
                    }
                }

                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.fillMaxSize().padding(50.dp),
                        painter = painterResource(R.drawable.star),
                        tint = Color.DarkGray,
                        contentDescription = null
                    )
                }
            }
        }
    )
}