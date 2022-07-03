package com.example.github.vm.base

import androidx.lifecycle.viewModelScope
import com.example.github.data.remote.ResponseResult
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseApiViewModel<Data, Model>(
    override val tag: String,
    protected val mapper: (Model) -> Data
): BaseViewModel<ResponseResult.ResponseError>(tag, ResponseResult.None) {

    protected val _data: MutableStateFlow<Data?> = MutableStateFlow(null)
    val data: StateFlow<Data?> = _data

    fun fetchData(fetch: suspend () -> ResponseResult<Model>) {
        AppLogger.log(tag, "Fetching data")

        _isLoading.value = true

        viewModelScope.launch {
            val result = try {
                withContext(dispatcher) {
                    fetch()
                }
            } catch (ex: Exception) {
                AppLogger.log(tag, "Exception: ${ex.message}", LogType.Error)
                ResponseResult.ExceptionResponseError(ex)
            } finally {
                _isLoading.value = false
            }

            when (result) {
                is ResponseResult.Success -> {
                    AppLogger.log(tag, "Fetched data: $data")
                    onData(mapper(result.model))
                }
                is ResponseResult.ResponseError -> {
                    AppLogger.log(tag, "Fetched error: $error", LogType.Error)
                    onError(result)
                }
            }
        }
    }

    protected open fun onData(data: Data) {
        _data.value = data
    }

    protected open fun onError(error: ResponseResult.ResponseError) {
        _error.value = error
    }

    override fun onCleared() {
        AppLogger.log(tag, "Clear VM")
        super.onCleared()
    }
}