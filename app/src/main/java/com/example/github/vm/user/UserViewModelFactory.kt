package com.example.github.vm.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.data.data.UserData
import com.example.github.util.Constants

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val userData: UserData): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java))
            UserViewModel(userData = userData) as T
        else
            throw IllegalArgumentException(Constants.VIEW_MODEL_ERROR)
    }
}