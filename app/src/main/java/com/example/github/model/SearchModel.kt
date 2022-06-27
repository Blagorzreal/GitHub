package com.example.github.model

import com.google.gson.annotations.SerializedName

data class SearchModel(@SerializedName("items") val items: List<UserModel>)