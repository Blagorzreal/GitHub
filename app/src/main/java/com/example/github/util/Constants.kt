package com.example.github.util

import com.google.gson.Gson

class Constants private constructor() {
    companion object {
        const val EMPTY_STRING = ""
        const val HTTP_ERROR_NOT_FOUND = 404
        val GSON = Gson()
    }
}