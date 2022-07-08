package com.example.github.vm.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T>(
    protected val initialError: T,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO): AbstractViewModel(dispatcher) {

    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    protected val _error: MutableStateFlow<T> = MutableStateFlow(initialError)
    val error: StateFlow<T> = _error

    fun resetError() {
        _error.value = initialError
    }
}