package com.example.github.data.preference

import android.content.Context
import com.example.github.data.data.UserData
import com.example.github.util.Constants.Companion.GSON

class LoginPreferences(context: Context, name: String): BasePreferences(context, name) {
    companion object {
        private const val USER_DATA = "USER_DATA"
    }

    private var _userData: UserData? = null

    val userData: UserData? get() {
        if (_userData != null)
            return _userData

        val userData = sharedPreferences.getString(USER_DATA, null)
        return if (userData.isNullOrBlank())
            null
        else
            GSON.fromJson(userData, UserData::class.java)
    }

    fun setUserData(userData: UserData?) {
        _userData = userData

        if (userData == null)
            sharedPreferences.edit().remove(USER_DATA).apply()
        else
            sharedPreferences.edit().putString(USER_DATA, GSON.toJson(userData)).apply()
    }

    fun clean() {
        setUserData(null)
    }
}