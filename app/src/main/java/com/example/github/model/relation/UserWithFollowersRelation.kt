package com.example.github.model.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.github.model.UserModel

data class UserWithFollowersRelation(
    @Embedded val userModel: UserModel,
    @Relation(
        parentColumn = "owner_id",
        entityColumn = "owner_id",
        associateBy = Junction(
            value = UserWithFollowersRef::class,
            parentColumn = "id1",
            entityColumn = "id2"
        )
    )
    val followers: List<UserModel>
)