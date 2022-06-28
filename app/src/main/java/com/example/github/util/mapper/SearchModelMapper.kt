package com.example.github.util.mapper

import com.example.github.data.data.SearchData
import com.example.github.model.SearchModel
import com.example.github.util.mapper.UserModelMapper.Companion.userModelToUserData

class SearchModelMapper private constructor() {
    companion object {
        fun searchModelListToSearchDataList(searchModel: SearchModel) =
            SearchData(
                searchModel.totalCount ?: 0,
                searchModel.items.map { userModelToUserData(it) })
    }
}