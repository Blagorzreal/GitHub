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

    protected sealed class ProceededDataResult<out T> {
        object Ignore: ProceededDataResult<Nothing>()
        data class Set<T>(val data: T): ProceededDataResult<T>()
    }

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
                is ResponseResult.Success -> onData(mapper(result.model))
                is ResponseResult.ResponseError -> onError(result)
            }
        }
    }

    protected open fun proceedData(data: Data): ProceededDataResult<Data> =
        ProceededDataResult.Set(data)

    protected open fun onData(data: Data) {
        AppLogger.log(tag, "Fetched data: $data")

        val proceededDataResult = proceedData(data)
        if (proceededDataResult is ProceededDataResult.Set<Data>)
            _data.value = proceededDataResult.data
    }

    protected open fun onError(error: ResponseResult.ResponseError) {
        AppLogger.log(tag, "Fetched error: $error", LogType.Error)
        _error.value = error
    }

    override fun onCleared() {
        AppLogger.log(tag, "Clear VM")
        super.onCleared()
    }
}