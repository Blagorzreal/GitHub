package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.UsersRepository
import com.example.github.data.data.SearchData
import com.example.github.model.SearchModel
import com.example.github.util.Constants
import com.example.github.util.log.AppLogger
import com.example.github.util.mapper.SearchModelMapper
import com.example.github.vm.base.BaseApiViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(
    private val usersRepository: UsersRepository = UsersRepository()
): BaseApiViewModel<SearchData, SearchModel>(TAG, SearchModelMapper::searchModelListToSearchDataList) {
    companion object {
        private const val TAG = "Search VM"
    }

    init {
        viewModelScope.launch {
            usersRepository.localUsers.collectLatest {
                AppLogger.log(tag, "Local users changed: ${it.items.size}")
                _data.value = mapper(it)
            }
        }
    }

    private val _searchText = MutableStateFlow(Constants.EMPTY_STRING)
    val searchText: StateFlow<String> = _searchText

    fun onSearchTextChanged(text: String) {
        AppLogger.log(TAG, "Search text changed to $text")
        _searchText.value = text
    }

    fun search() {
        AppLogger.log(TAG, "Search")

        val searchTrimmed = _searchText.value.trim()
        fetchData { usersRepository.search(searchTrimmed) }
    }
}