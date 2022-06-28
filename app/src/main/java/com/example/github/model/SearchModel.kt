package com.example.github.model

import com.google.gson.annotations.SerializedName

data class SearchModel(
    @SerializedName("total_count") val totalCount: Int? = null,
    @SerializedName("items") val items: List<UserModel>)