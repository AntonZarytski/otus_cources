package com.otus.homework

import android.util.Log
import com.otus.homework.networkUtils.NetworkResult
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(private val catsService: CatsService) {

    private val timeoutMills: Long = 10000L

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            when (exception) {
                is SocketTimeoutException -> catsView.connectionError(exception.message)
                else -> Log.e("exceptionHandler", ": ", exception)
            }
        }
    }

    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + exceptionHandler + SupervisorJob())

    private var jobs: MutableList<Job> = mutableListOf()

    private var _catsView: ICatsView? = null
    private val catsView: ICatsView
        get() = requireNotNull(_catsView) { "VIew not provided" }

    fun onInitComplete() {
        launchRequest {
            toIoThread {
                val result = catsService.getCatFact()
                toMainThread {
                    when (result) {
                        is NetworkResult.Success<Fact> -> {
                            catsView.populate(result.body)
                        }
                        is NetworkResult.Fail -> {
                            throw result.error
                        }
                    }
                }
//                throw RuntimeException("test")
            }
        }
        launchRequest {
            toIoThread {
                withTimeoutOrNull(timeoutMills) {
                    val result = catsService.getCatImage()
                    toMainThread {
                        when (result) {
                            is NetworkResult.Success<Image> -> {
                                catsView.setImage(result.body.url)
                            }
                            is NetworkResult.Fail -> {
                                throw result.error
                            }
                        }
                    }
//                    throw RuntimeException("test")
                }
            }
        }
    }


    private fun launchRequest(command: suspend () -> Unit) {
        jobs.add(presenterScope.launch {
            withTimeoutOrNull(timeoutMills) {
                try {
                    command.invoke()
                } catch (e: Exception) {
                    CrashMonitor.trackWarning(e)
                    toMainThread {
                        catsView.connectionError(e.message)
                    }
                }
            } ?: throw SocketTimeoutException("Не удалось получить ответ от сервера")
        })
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        for (job in jobs) {
            if (job.isActive) {
                job.cancel()
            }
        }
        jobs.clear()
    }
}