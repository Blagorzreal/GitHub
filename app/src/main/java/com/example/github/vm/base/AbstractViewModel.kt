package com.example.github.vm.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class AbstractViewModel(
    protected open val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {
    abstract val tag: String
}