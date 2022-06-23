package com.example.github.model

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

data class RepoModel(
    @NotNull @SerializedName("id") val id: Long,
    @NotNull @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: UserModel?)