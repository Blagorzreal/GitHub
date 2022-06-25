package com.example.github.ui.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun RepositoryScreen(owner: String, repo: String) {
    Text("Hey, $owner of $repo")
}