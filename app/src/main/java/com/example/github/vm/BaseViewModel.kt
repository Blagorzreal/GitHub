package com.example.github.vm

import com.example.github.util.log.AppLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T>(
    protected override val tag: String,
    protected val initialError: T,
    open override val dispatcher: CoroutineDispatcher = Dispatchers.IO): AbstractViewModel(tag) {

    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    protected val _error: MutableStateFlow<T> = MutableStateFlow(initialError)
    val error: StateFlow<T> = _error

    init {
        AppLogger.log(tag, "Init VM")
    }

    fun resetError() {
        _error.value = initialError
    }
}