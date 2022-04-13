package com.otus.homework.networkUtils

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import java.io.IOException

class CustomResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<NetworkResult<S>> {

    override fun enqueue(callback: Callback<NetworkResult<S>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@CustomResponseCall,
                            Response.success(NetworkResult.Success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@CustomResponseCall,
                            Response.success(NetworkResult.Fail(IOException("Response is empty")))
                        )
                    }
                } else {
                    Response.success(NetworkResult.Fail(IOException("Invalid response")))
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                Response.success(NetworkResult.Fail(IOException("Invalid response")))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = CustomResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResult<S>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}