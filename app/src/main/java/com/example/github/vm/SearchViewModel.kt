package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.SearchRepository
import com.example.github.data.data.SearchData
import com.example.github.data.data.UserData
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
    private val searchRepository: SearchRepository = SearchRepository()
): BaseApiViewModel<SearchData, SearchModel>(TAG, SearchModelMapper::searchModelListToSearchDataList) {
    companion object {
        private const val TAG = "Search VM"
        private const val USERS_PER_PAGE = 30
    }

    private var nextRemotePage = 1
    private var totalRemotePages = 1

    private var nextLocalPage = 1
    private var totalLocalPages = 1

    private val _hasMorePages: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages

    private val remoteItems = mutableListOf<UserData>()
    private val localItems = mutableListOf<UserData>()

    private val _items: MutableStateFlow<List<UserData>?> = MutableStateFlow(null)
    val items: StateFlow<List<UserData>?> = _items

    init {
        viewModelScope.launch {
            searchRepository.localSearch.collectLatest {
                if (it?.totalCount == null) {
                    totalLocalPages = 1
                    return@collectLatest
                }

                totalLocalPages = refreshPagesCount(it.totalCount)

                val newItems = mapper(it).items
                if (newItems.isNotEmpty()) {
                    nextLocalPage++

                    AppLogger.log(tag, "Search users changed: ${it.totalCount}")

                    localItems += newItems
                }

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

        AppLogger.log(TAG, "Load page: $nextRemotePage of $totalRemotePages, $nextLocalPage of $totalLocalPages")

        if (searchAvailable) {
            fetchData {
                searchRepository.search(
                    _searchText.value.trim(),
                    nextRemotePage,
                    nextLocalPage,
                    USERS_PER_PAGE
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

        nextRemotePage++

        remoteItems += data.items
        _items.value = remoteItems

        updateHasMorePages()
    }

    override fun onError(error: ResponseResult.ResponseError) {
        super.onError(error)
        _items.value = localItems
    }

    private fun updateHasMorePages() {
        _hasMorePages.value = searchAvailable
    }

    private val searchAvailable get() =
        (totalRemotePages >= nextRemotePage) || (totalLocalPages >= nextLocalPage)

    private fun reset() {
        nextRemotePage = 1
        totalRemotePages = 1

        nextLocalPage = 1
        totalLocalPages = 1

        localItems.clear()
        remoteItems.clear()
        _items.value = listOf()

        _hasMorePages.value = false
    }

    private fun refreshPagesCount(totalCount: Int): Int {
        var pages = (totalCount / USERS_PER_PAGE)
        if ((totalCount % USERS_PER_PAGE) > 0)
            pages++

        return pages
    }
}