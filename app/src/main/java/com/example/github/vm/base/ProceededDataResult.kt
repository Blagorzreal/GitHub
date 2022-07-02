package com.example.github.vm.base

sealed class ProceededDataResult<out T> {
    object IgnoreDataResult: ProceededDataResult<Nothing>()
    data class SetDataResult<T>(val data: T): ProceededDataResult<T>()
}