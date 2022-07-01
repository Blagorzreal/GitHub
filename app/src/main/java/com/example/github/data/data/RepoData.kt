package com.example.github.data.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoData(
    val id: Long,
    val name: String,
    val owner: UserData,
    var starred: Boolean = false) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RepoData

        if (id != other.id) return false

        return owner == other.owner
    }

    override fun hashCode() = owner.hashCode() + id.hashCode()
}