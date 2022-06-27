package com.example.github.vm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.data.data.UserData
import com.example.github.util.Constants
import com.example.github.vm.ReposViewModel
import com.example.github.vm.StarredReposViewModel
import com.example.github.vm.UserViewModel

enum class ViewModelType {
    Repos,
    StarredRepos,
    User,
}

@Suppress("UNCHECKED_CAST")
class UserDataViewModelFactory(
    private val userData: UserData,
    private val viewModelType: ViewModelType
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (viewModelType) {
            ViewModelType.Repos -> createVMInstance(modelClass) { ReposViewModel(userData = userData) }
            ViewModelType.StarredRepos -> createVMInstance(modelClass) { StarredReposViewModel(userData) }
            ViewModelType.User -> createVMInstance(modelClass) { UserViewModel(userData) }
        }

    private fun <T: ViewModel> createVMInstance(modelClass: Class<T>, vmClass: () -> BaseViewModel<*>) =
        if (BaseViewModel::class.java.isAssignableFrom(modelClass))
            vmClass() as T
        else
            throw IllegalArgumentException(Constants.VIEW_MODEL_ERROR)
}