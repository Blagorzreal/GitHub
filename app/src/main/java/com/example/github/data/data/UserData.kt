package com.example.github.data.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val id: Long,
    val username: String,
    val avatarUrl: String?,
    var followers: Long?,
    var following: Long?) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserData

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id.hashCode()
}