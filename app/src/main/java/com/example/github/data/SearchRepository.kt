package com.example.github.data

import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.search.ISearchApi
import com.example.github.model.SearchModel
import com.example.github.util.log.AppLogger
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class SearchRepository @Inject constructor() {

    companion object {
        private const val TAG = "Search repo"
    }

    @Inject lateinit var userDao: IUserDao
    @Inject lateinit var searchApi: ISearchApi

    private var _localSearch: MutableStateFlow<SearchModel?> = MutableStateFlow(null)
    var localSearch: Flow<SearchModel?> = _localSearch

    suspend fun search(
        username: String,
        remotePage: Int,
        localPage: Int,
        usersPerPage: Int
    ): ResponseResult<SearchModel> {
        AppLogger.log(TAG, "Search")

        val offset = (localPage - 1) * usersPerPage
        val usernameCriteria = "$username%"

        val localUsers = userDao.searchByUsername(usernameCriteria, offset, usersPerPage)
        val totalCount = userDao.getColumnCountByUsername(usernameCriteria)
        _localSearch.value = SearchModel(totalCount, localUsers)

        val result = searchApi.search(usersPerPage, remotePage, username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote users to the db")

            userDao.insertUsers(result.model.items)
        }

        return result
    }
}