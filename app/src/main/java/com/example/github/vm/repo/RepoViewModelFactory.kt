package com.example.github.vm.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.data.data.RepoData
import com.example.github.util.Constants

@Suppress("UNCHECKED_CAST")
class RepoViewModelFactory(private val repoData: RepoData): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RepoViewModelData::class.java))
            RepoViewModelData(repoData) as T
        else
            throw IllegalArgumentException(Constants.VIEW_MODEL_ERROR)
    }
}