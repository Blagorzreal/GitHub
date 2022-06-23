package com.example.github.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.data.remote.ResponseResult
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<Data, Model>(
    private val mapper: (Model) -> Data,
    private val tag: String,
    protected val coroutineScope: CoroutineDispatcher = Dispatchers.IO): ViewModel() {

    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    protected val _responseError: MutableStateFlow<ResponseResult.ResponseError?> = MutableStateFlow(null)
    val responseError: StateFlow<ResponseResult.ResponseError?> = _responseError

    protected val _data: MutableStateFlow<Data?> = MutableStateFlow(null)
    val data: StateFlow<Data?> = _data

    init {
        AppLogger.log(tag, "Init VM")
    }

    open fun onData(data: Data) { }

    open fun onError(error: ResponseResult.ResponseError) { }

    protected fun fetchData(fetch: suspend () -> ResponseResult<Model>) {
        AppLogger.log(tag, "Fetching data")

        _isLoading.value = true

        viewModelScope.launch {
            val result = withContext(coroutineScope) { fetch() }
            _isLoading.value = false

            when (result) {
                is ResponseResult.Success -> {
                    AppLogger.log(tag, "Fetched data successfully")

                    mapper(result.data).let {
                        _data.value = it
                        onData(it)
                    }
                }
                is ResponseResult.ResponseError -> {
                    AppLogger.log(tag, "Unable to fetch the data: $result")

                    _responseError.value = result
                    onError(result)
                }
            }
        }
    }

    override fun onCleared() {
        AppLogger.log(tag, "Clear VM")
        super.onCleared()
    }
}