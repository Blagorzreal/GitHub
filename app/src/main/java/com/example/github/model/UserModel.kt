package com.example.github.model

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

data class UserModel(
    @NotNull @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String)