package com.example.github.data.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoData(
    val id: Long,
    val name: String,
    val owner: UserData,
    var starred: Boolean = false) : Parcelable