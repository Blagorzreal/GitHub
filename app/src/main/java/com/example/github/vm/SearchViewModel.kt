package com.example.github.vm

import com.example.github.data.UsersRepository
import com.example.github.data.data.SearchData
import com.example.github.model.SearchModel
import com.example.github.util.mapper.SearchModelMapper
import com.example.github.vm.base.BaseApiViewModel

class SearchViewModel(
    private val usersRepository: UsersRepository = UsersRepository()
): BaseApiViewModel<SearchData, SearchModel>(TAG, SearchModelMapper::searchModelListToSearchDataList) {
    companion object {
        private const val TAG = "Search VM"
    }

    
}