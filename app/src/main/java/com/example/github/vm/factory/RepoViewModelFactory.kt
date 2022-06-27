package com.example.github.vm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.data.data.RepoData
import com.example.github.util.Constants
import com.example.github.vm.RepoViewModel

@Suppress("UNCHECKED_CAST")
class RepoViewModelFactory(private val repoData: RepoData): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RepoViewModel::class.java))
            RepoViewModel(repoData) as T
        else
            throw IllegalArgumentException(Constants.VIEW_MODEL_ERROR)
    }
}