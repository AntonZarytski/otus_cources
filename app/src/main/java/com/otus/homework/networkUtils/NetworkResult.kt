package com.otus.homework.networkUtils

sealed class NetworkResult<out T : Any> {

    data class Success<T : Any>(val body: T) : NetworkResult<T>()

    data class Fail(val error: Throwable) : NetworkResult<Nothing>()
}