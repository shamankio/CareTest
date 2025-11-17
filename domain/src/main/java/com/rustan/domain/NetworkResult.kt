package com.rustan.domain

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    sealed class Error : NetworkResult<Nothing>() {
        data class ApiError(val code: Int, val message: String) : Error()
        data class Exception(val message: String?) : Error()
    }
}