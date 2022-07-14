package com.example.github.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity
data class UserModel(
    @PrimaryKey
    @NotNull
    @SerializedName("id") val id: Long,

    @NotNull
    @SerializedName("login")
    val login: String,

    @SerializedName("followers")
    val followers: Long?,

    @SerializedName("following")
    val following: Long?,

    @SerializedName("avatar_url") val avatarUrl: String?)