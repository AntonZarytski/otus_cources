package com.otus.homework

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            when (exception) {
                is SocketTimeoutException -> catsView.connectionError(exception.message)
                else -> Log.e("exceptionHandler", ": ", exception)
            }
        }
    }

    private val presenterScope by lazy {
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + exceptionHandler + SupervisorJob())
    }

    private var ioJob: Job? = null

    private var _catsView: ICatsView? = null
    private val catsView: ICatsView
        get() = requireNotNull(_catsView) { "VIew not provided" }

    fun onInitComplete() {
        ioJob = presenterScope.launch {
            ioThread {
                withTimeoutOrNull(50) {
                    catsService.getCatFact()
                        .onSuccess {
                            mainThread {
                                catsView.populate(it)
                            }
                            throw ArithmeticException()
                        }.onFailure {
                            CrashMonitor.trackWarning(it)
                            mainThread {
                                catsView.connectionError(it.message)
                            }
                        }
                } ?: throw SocketTimeoutException("Не удалось получить ответ от сервера")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        ioJob?.apply {
            if (isActive) {
                cancel()
            }
        }
    }
}