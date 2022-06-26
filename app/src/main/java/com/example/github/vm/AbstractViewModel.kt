package com.example.github.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class AbstractViewModel(
    protected open val tag: String,
    protected open val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel()