package com.example.github.data

import com.example.github.data.local.DaoProvider
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.search.ISearchApi
import com.example.github.model.SearchModel
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchRepository(
    protected val userDao: IUserDao = DaoProvider.userDao,
    protected val searchApi: ISearchApi = ApiProvider.searchApi
) {
    companion object {
        private const val TAG = "Search repo"
    }

    private var _localSearch: MutableStateFlow<SearchModel?> = MutableStateFlow(null)
    var localSearch: Flow<SearchModel?> = _localSearch

    suspend fun search(
        usersPerPage: Int,
        remotePage: Int,
        localPage: Int,
        username: String
    ): ResponseResult<SearchModel> {
        AppLogger.log(TAG, "Search")

        val offset = (localPage - 1) * usersPerPage
        val usernameCriteria = "$username%"

        val localUsers = userDao.searchByUsername(usernameCriteria, offset, usersPerPage)
        val totalCount = userDao.searchColumnCountByUsername(usernameCriteria)
        _localSearch.value = SearchModel(totalCount, localUsers)

        val result = searchApi.search(usersPerPage, remotePage, username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote users to the db")

            userDao.insertUsers(result.model.items)
        }

        return result
    }
}