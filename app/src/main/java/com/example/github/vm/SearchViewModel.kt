package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.UsersRepository
import com.example.github.data.data.SearchData
import com.example.github.data.remote.ResponseResult
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
        private const val USERS_PER_PAGE = 30
    }

    private var totalPages = 1
    private var currentPage = 1

    private val _hasNextPage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage

    init {
        viewModelScope.launch {
            usersRepository.localSearch.collectLatest {
                if (it == null)
                    return@collectLatest

                AppLogger.log(tag, "Users changed: ${it.totalCount}")
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

    fun searchNextPage() {
        if (isLoading.value)
            return

        AppLogger.log(TAG, "Search page: $currentPage of $totalPages")

        val searchTrimmed = _searchText.value.trim()
        if (totalPages >= currentPage) {
            val currentPageCached = currentPage
            fetchData { usersRepository.search(USERS_PER_PAGE, currentPageCached, searchTrimmed) }
            currentPage++
        }
    }

    fun search() {
        if (isLoading.value)
            return

        AppLogger.log(TAG, "Search")

        totalPages = 1
        currentPage = 1

        _hasNextPage.value = false

        searchNextPage()
    }

    override fun onData(data: SearchData) {
        super.onData(data)

        totalPages = (data.totalCount / USERS_PER_PAGE)
        if ((data.totalCount % USERS_PER_PAGE) > 0)
            totalPages++

        _hasNextPage.value = (totalPages >= currentPage)
    }

    override fun onError(error: ResponseResult.ResponseError) {
        super.onError(error)

        if (--currentPage < 1)
            currentPage = 1
    }
}