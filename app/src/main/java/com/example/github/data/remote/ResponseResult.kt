package com.example.github.data.remote

sealed class ResponseResult<out T> {
    abstract class ResponseError: ResponseResult<Nothing>()

    object None: ResponseError()
    object NoInternetConnectionAvailable: ResponseError()
    object InvalidResponseError: ResponseError()
    object NullBodyResponseError: ResponseError()
    data class ExceptionResponseError(val exception: Exception): ResponseError()
    data class UnsuccessfulResponseError(val code: Int): ResponseError()
    data class Success<out T>(val model: T): ResponseResult<T>()
}