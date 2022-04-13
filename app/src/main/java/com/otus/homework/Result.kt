package com.otus.homework

import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

sealed class Result<Any> {

    class Success<Any>(val value: Any) : Result<Any>()

    class Error<Any>(val exception: Throwable?) : Result<Any>()
}