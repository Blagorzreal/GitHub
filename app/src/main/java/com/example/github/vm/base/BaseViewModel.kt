package com.example.github.vm.base

import com.example.github.util.log.AppLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T>(
    final override val tag: String,
    protected val initialError: T,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO): AbstractViewModel(tag, dispatcher) {

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