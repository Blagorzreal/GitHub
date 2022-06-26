package com.example.github.vm.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.data.data.UserData
import com.example.github.util.Constants

@Suppress("UNCHECKED_CAST")
class ReposViewModelFactory(private val userData: UserData): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReposViewModelData::class.java))
            ReposViewModelData(userData = userData) as T
        else
            throw IllegalArgumentException(Constants.VIEW_MODEL_ERROR)
    }
}