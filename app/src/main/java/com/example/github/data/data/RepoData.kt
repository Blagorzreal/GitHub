package com.example.github.data.data

data class RepoData(
    val id: Long,
    val name: String,
    val owner: UserData,
    var starred: Boolean = false)