package com.example.github.util

import com.example.github.model.RepoModel
import com.example.github.model.UserModel
import com.example.github.model.relation.RepoWithOwnerRelation
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class RepoModelDeserializer: JsonDeserializer<RepoWithOwnerRelation?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RepoWithOwnerRelation? {
        try {
            json?.asJsonObject?.also {
                val owner = Constants.GSON.fromJson(it.get("owner"), UserModel::class.java)

                return RepoWithOwnerRelation(
                    RepoModel(
                        it.get("id").asLong,
                        owner.id,
                        it.get("name").asString
                    ), owner)
            }
        } catch (ex: Exception) {
            AppLogger.log("RepoModelDeserializer", ex.message ?: Constants.EMPTY_STRING, LogType.Error)
        }

        return null
    }
}