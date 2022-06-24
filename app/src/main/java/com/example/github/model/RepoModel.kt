package com.example.github.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity
data class RepoModel(
    @NotNull @PrimaryKey @SerializedName("id") val id: Long,
    @NotNull @SerializedName("name") val name: String,
    @Embedded @SerializedName("owner") val owner: UserModel?,
    @ColumnInfo(name = "starred") val starred: Boolean = false)