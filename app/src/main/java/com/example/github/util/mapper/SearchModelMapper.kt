package com.example.github.util.mapper

import com.example.github.data.data.SearchData
import com.example.github.model.SearchModel

class SearchModelMapper private constructor() {
    companion object {
        fun searchModelListToSearchDataList(searchModel: SearchModel) =
            SearchData(searchModel.items.map { UserModelMapper.userModelToUserData(it) })
    }
}