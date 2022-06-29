package com.example.github.model.relation

import androidx.room.Entity

@Entity(primaryKeys = ["id1", "id2"])
data class UserWithFollowersRef(val id1: Long, val id2: Long)