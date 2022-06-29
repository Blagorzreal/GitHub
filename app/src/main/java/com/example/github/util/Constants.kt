package com.example.github.util

import com.google.gson.Gson

class Constants private constructor() {
    companion object {
        const val USERS = "users"
        const val USERNAME = "username"
        const val EMPTY_STRING = ""
        const val HTTP_ERROR_NOT_FOUND = 404
        const val HTTP_FORBIDDEN = 403
        val GSON = Gson()
        const val VIEW_MODEL_ERROR = "ViewModel Not Found"
    }
}