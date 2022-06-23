package com.example.github.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity
data class UserModel(
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "owner_id")
    @SerializedName("id") val id: Long,

    @NotNull
    @SerializedName("login")
    val login: String)