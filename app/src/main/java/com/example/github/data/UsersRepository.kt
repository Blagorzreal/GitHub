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

class UsersRepository(
    protected val userDao: IUserDao = DaoProvider.userDao,
    protected val searchApi: ISearchApi = ApiProvider.searchApi
) {
    companion object {
        private const val TAG = "Users repo"
    }

    private var _localUsers: MutableStateFlow<SearchModel> = MutableStateFlow(SearchModel(emptyList()))
    var localUsers: Flow<SearchModel> = _localUsers

    suspend fun search(text: String): ResponseResult<SearchModel> {
        AppLogger.log(TAG, "Search")

        _localUsers.value = SearchModel(userDao.searchByUsername("%$text%"))

        val result = searchApi.search(text)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert users to the db")

            _localUsers.value = SearchModel(
                userDao.insertUsersAndSearchByUsername(result.model.items, "%$text%"))
        }

        return result
    }
}