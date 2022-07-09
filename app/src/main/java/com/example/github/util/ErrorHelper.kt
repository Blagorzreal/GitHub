package com.example.github.util

import com.example.github.data.remote.ResponseResult

class ErrorHelper private constructor() {
    companion object {
        fun isUsernameNotFoundError(responseError: ResponseResult.ResponseError) =
            (responseError is ResponseResult.UnsuccessfulResponseError) &&
                    (responseError.code == Constants.HTTP_ERROR_NOT_FOUND)

        fun isForbiddenError(responseError: ResponseResult.ResponseError) =
            (responseError is ResponseResult.UnsuccessfulResponseError) &&
                    (responseError.code == Constants.HTTP_FORBIDDEN)

        fun isNoInternetConnectionError(responseError: ResponseResult.ResponseError) =
            (responseError is ResponseResult.NoInternetConnectionAvailable)
    }
}