package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.SearchRepository
import com.example.github.data.data.SearchData
import com.example.github.data.data.UserData
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
    private val searchRepository: SearchRepository = SearchRepository()
): BaseApiViewModel<SearchData, SearchModel>(TAG, SearchModelMapper::searchModelListToSearchDataList) {
    companion object {
        private const val TAG = "Search VM"
        private const val USERS_PER_PAGE = 30
    }

    private var currentRemotePage = 1
    private var totalRemotePages = 1

    private var currentLocalPage = 1
    private var totalLocalPages = 1

    private val _hasMorePages: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages

    private val _items: MutableStateFlow<MutableSet<UserData>?> = MutableStateFlow(null)
    val items: StateFlow<Set<UserData>?> = _items

    init {
        viewModelScope.launch {
            searchRepository.localSearch.collectLatest {
                if (it == null) {
                    totalLocalPages = 1
                    return@collectLatest
                }

                totalLocalPages = if (it.totalCount != null)
                    refreshPagesCount(it.totalCount)
                else
                    1

                AppLogger.log(tag, "Search users changed: ${it.totalCount}")
                _items.value?.plusAssign(mapper(it).items)

                updateHasMorePages()
            }
        }
    }

    private val _searchText = MutableStateFlow(Constants.EMPTY_STRING)
    val searchText: StateFlow<String> = _searchText

    fun onSearchTextChanged(text: String) {
        AppLogger.log(TAG, "Search text changed to $text")
        _searchText.value = text
    }

    fun loadNextPage() {
        if (isLoading.value)
            return

        AppLogger.log(TAG, "Load page: $currentRemotePage of $totalRemotePages")

        if (searchAvailable) {
            fetchData {
                searchRepository.search(
                    USERS_PER_PAGE,
                    currentRemotePage,
                    currentLocalPage++,
                    _searchText.value.trim()
                )
            }
        }
    }

    fun search() {
        if (isLoading.value)
            return

        AppLogger.log(TAG, "Search")

        reset()
        loadNextPage()
    }

    override fun onData(data: SearchData) {
        super.onData(data)

        totalRemotePages = refreshPagesCount(data.totalCount)

        currentRemotePage++

        _items.value?.plusAssign(data.items)

        updateHasMorePages()
    }

    private fun updateHasMorePages() {
        _hasMorePages.value = searchAvailable
    }

    private val searchAvailable get() =
        (totalRemotePages >= currentRemotePage) || (totalLocalPages >= currentLocalPage)

    private fun reset() {
        currentRemotePage = 1
        totalRemotePages = 1

        currentLocalPage = 1
        totalLocalPages = 1

        _items.value = mutableSetOf()

        _hasMorePages.value = false
    }

    private fun refreshPagesCount(totalCount: Int): Int {
        var pages = (totalCount / USERS_PER_PAGE)
        if ((totalCount % USERS_PER_PAGE) > 0)
            pages++

        return pages
    }
}