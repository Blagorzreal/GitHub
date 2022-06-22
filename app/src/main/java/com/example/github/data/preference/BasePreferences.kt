package com.example.github.data.preference

import android.content.Context
import android.content.SharedPreferences

abstract class BasePreferences(context: Context, name: String) {
    protected val sharedPreferences: SharedPreferences

    init {
        sharedPreferences =
            context.getSharedPreferences(
                name,
                Context.MODE_PRIVATE)
    }
}