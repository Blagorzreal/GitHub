package com.example.github.data.remote

sealed class ResponseResult<out T> {
    abstract class ResponseError(var handled: Boolean = false): ResponseResult<Nothing>()

    object InvalidResponseError: ResponseError()
    object NullBodyResponseError: ResponseError()
    data class ExceptionResponseError(val exception: Exception): ResponseError()
    data class UnsuccessfulResponseError(val code: Int): ResponseError()
    data class Success<out T>(val data: T): ResponseResult<T>()
}