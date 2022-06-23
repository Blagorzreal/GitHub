package com.example.github.vm.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.data.data.UserData
import com.example.github.util.Constants.Companion.VIEW_MODEL_ERROR

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private val userData: UserData): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            ProfileViewModel(userData) as T
        else
            throw IllegalArgumentException(VIEW_MODEL_ERROR)
    }
}