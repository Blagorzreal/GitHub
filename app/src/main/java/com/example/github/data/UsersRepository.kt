package com.example.github.data

import com.example.github.data.local.DaoProvider
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.search.ISearchApi

class UsersRepository(
    protected val userDao: IUserDao = DaoProvider.userDao,
    protected val searchApi: ISearchApi = ApiProvider.searchApi
) {
    companion object {
        private const val TAG = "Users repo"
    }


}