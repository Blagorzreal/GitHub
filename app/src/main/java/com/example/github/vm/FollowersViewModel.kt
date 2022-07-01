package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.FollowersRepository
import com.example.github.data.data.UserData
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import com.example.github.util.mapper.UserModelMapper
import com.example.github.vm.base.BaseApiViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FollowersViewModel(
    private val userData: UserData,
    private val followersRepository: FollowersRepository = FollowersRepository(userData)
): BaseApiViewModel<Set<UserData>, List<UserModel>>(TAG, UserModelMapper::userModelListToUserDataSet) {
    companion object {
        private const val TAG = "Followers VM"
    }

    init {
        viewModelScope.launch {
            followersRepository.localFollowers.collectLatest {
                AppLogger.log(tag, "Local followers changed: ${it.followers}")
                _data.value = mapper(it.followers)
            }
        }

        updateFollowers()
    }

    fun updateFollowers() {
        AppLogger.log(tag, "Update followers")
        fetchData { followersRepository.updateFollowers() }
    }
}