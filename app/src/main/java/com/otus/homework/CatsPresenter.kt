package com.otus.homework

import android.util.Log
import com.otus.homework.networkUtils.NetworkResult
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(private val catsService: CatsService) {

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            when (exception) {
                is SocketTimeoutException -> catsView.connectionError("Не удалось получить ответ от сервера")
                else -> Log.e("exceptionHandler", ": ", exception)
            }
        }
    }

    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + exceptionHandler + SupervisorJob())

    private var _catsView: ICatsView? = null
    private val catsView: ICatsView
        get() = requireNotNull(_catsView) { "VIew not provided" }

    fun onInitComplete() {
        launchRequest {
            when (val result = catsService.getCatFact()) {
                is NetworkResult.Success<Fact> -> {
                    catsView.populate(result.body)
                }
                is NetworkResult.Fail -> {
                    throw result.error
                }
            }
//            throw RuntimeException("test")
        }
        launchRequest {
            when (val result = catsService.getCatImage()) {
                is NetworkResult.Success<Image> -> {
                    catsView.setImage(result.body.url)
                }
                is NetworkResult.Fail -> {
                    throw result.error
                }
            }
//            throw RuntimeException("test")
        }
    }


    private fun launchRequest(command: suspend () -> Unit) {
        presenterScope.launch {
            try {
                command.invoke()
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                toMainThread {
                    catsView.connectionError(e.message)
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}