package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.UsersRepository
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
    private val usersRepository: UsersRepository = UsersRepository()
): BaseApiViewModel<SearchData, SearchModel>(TAG, SearchModelMapper::searchModelListToSearchDataList) {
    companion object {
        private const val TAG = "Search VM"
        private const val USERS_PER_PAGE = 30
    }

    private var currentRemotePage = 1
    private var totalRemotePages = 1

    private var currentLocalPage = 1
    private var totalLocalPages = 1

    private val _hasNextPage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage

    private val _items = MutableStateFlow(setOf<UserData>())
    val items: StateFlow<Set<UserData>> = _items

    init {
        viewModelScope.launch {
            usersRepository.localSearch.collectLatest {
                if (it == null)
                    return@collectLatest

                if (it.totalCount != null) {
                    totalLocalPages = (it.totalCount / USERS_PER_PAGE)
                    if ((it.totalCount % USERS_PER_PAGE) > 0)
                        totalLocalPages++
                } else
                    totalLocalPages = 1

                AppLogger.log(tag, "Search users changed: ${it.totalCount}")
                _items.value += mapper(it).items

                updateHasNextPage()
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

        AppLogger.log(TAG, "Search page: $currentRemotePage of $totalRemotePages")

        if (searchAvailable) {
            fetchData {
                usersRepository.search(
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

        currentRemotePage = 1
        totalRemotePages = 1

        currentLocalPage = 1
        totalLocalPages = 1

        _items.value = setOf()

        _hasNextPage.value = false

        searchNextPage()
    }

    override fun onData(data: SearchData) {
        super.onData(data)

        totalRemotePages = (data.totalCount / USERS_PER_PAGE)
        if ((data.totalCount % USERS_PER_PAGE) > 0)
            totalRemotePages++

        currentRemotePage++

        _items.value += data.items

        updateHasNextPage()
    }

    private fun updateHasNextPage() {
        _hasNextPage.value = searchAvailable
    }

    private val searchAvailable get() =
        (totalRemotePages >= currentRemotePage) || (totalLocalPages >= currentLocalPage)
}