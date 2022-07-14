package com.example.github.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.github.model.RepoModel
import com.example.github.model.UserModel

data class RepoWithOwnerRelation(
    @Embedded val repoModel: RepoModel,
    @Relation(
        parentColumn = "owner_id",
        entityColumn = "id"
    )
    val owner: UserModel
)