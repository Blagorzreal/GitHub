package com.example.github.vm.base

import androidx.lifecycle.viewModelScope
import com.example.github.data.remote.ResponseResult
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseApiViewModel<Data, Model>(
    tag: String,
    protected val mapper: (Model) -> Data
): BaseViewModel<ResponseResult.ResponseError>(tag, ResponseResult.None) {

    protected val _data: MutableStateFlow<Data?> = MutableStateFlow(null)
    val data: StateFlow<Data?> = _data

    fun fetchData(fetch: suspend () -> ResponseResult<Model>) {
        AppLogger.log(tag, "Fetching data")

        _isLoading.value = true

        viewModelScope.launch {
            val result = withContext(dispatcher) {
                try {
                    fetch()
                } catch (ex: Exception) {
                    AppLogger.log(tag, "Exception: ${ex.message}", LogType.Error)
                    ResponseResult.ExceptionResponseError(ex)
                }
            }

            try {
                when (result) {
                    is ResponseResult.Success -> onData(mapper(result.model))
                    is ResponseResult.ResponseError -> onError(result)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    protected open fun onData(data: Data) {
        AppLogger.log(tag, "Fetched data: $data")
        _data.value = data
    }

    protected open fun onError(error: ResponseResult.ResponseError)  {
        AppLogger.log(tag, "Fetched error: $error", LogType.Error)
        _error.value = error
    }

    override fun onCleared() {
        AppLogger.log(tag, "Clear VM")
        super.onCleared()
    }
}