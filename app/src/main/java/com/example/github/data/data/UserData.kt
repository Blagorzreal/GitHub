package com.example.github.data.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val id: Long,
    val username: String,
    val avatarUrl: String?,
    val followers: Long?,
    val following: Long?) : Parcelable