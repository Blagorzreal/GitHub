package com.example.github.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.github.data.FollowersRepository
import com.example.github.data.data.UserData
import com.example.github.model.UserModel
import com.example.github.ui.navigation.Route
import com.example.github.util.log.AppLogger
import com.example.github.util.mapper.UserModelMapper
import com.example.github.vm.base.BaseApiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    followersRepositoryFactory: FollowersRepository.FollowersRepositoryModuleFactory
): BaseApiViewModel<List<UserData>, List<UserModel>>(TAG, UserModelMapper::userModelListToUserDataList) {

    companion object {
        private const val TAG = "Followers VM"
    }

    private val followersRepository: FollowersRepository by lazy {
        followersRepositoryFactory.create(savedStateHandle.get<UserData>(Route.USER_DATA) ?: throw Exception())
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

    override fun onData(data: List<UserData>) {
        if (_data.value.isNullOrEmpty())
            _data.value = data.sortedBy { it.id }
    }
}