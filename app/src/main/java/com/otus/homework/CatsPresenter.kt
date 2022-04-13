package com.otus.homework

import android.util.Log
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
        CoroutineScope(Dispatchers.IO + CoroutineName("CatsCoroutine") + exceptionHandler + SupervisorJob())


    private var jobs: MutableList<Job> = mutableListOf()

    private var _catsView: ICatsView? = null
    private val catsView: ICatsView
        get() = requireNotNull(_catsView) { "VIew not provided" }

    fun onInitComplete() {
        launchRequest {
            toIoThread {
                val fact = catsService.getCatFact()
                toMainThread {
//                    catsView.populate(fact)
                }
//                throw RuntimeException("test")
//                TODO why doesnt work with Result < Fact >? Fact is null in onSuccess
//                catsService.getCatFact()
//                    .onSuccess {
//                        mainThread {
//                            catsView.populate(it)
//                        }
//                    }.onFailure {
//                        CrashMonitor.trackWarning(it)
//                        mainThread {
//                            catsView.connectionError(it.message)
//                        }
//                    }
            }
        }
        launchRequest {

            toIoThread {
                withTimeoutOrNull(timeoutMills) {
                    val image = catsService.getCatImage()
                    toMainThread {
//                        catsView.setImage(image.url)
                    }
//                    throw RuntimeException("test")
//                    TODO why doesnt work with Result < Image >? error 503 every time
//                    catsService.getCatImage()
//                        .onSuccess {
//                            mainThread {
//                                catsView.setImage(it.url)
//                            }
//                        }.onFailure {
//                            CrashMonitor.trackWarning(it)
//                            mainThread {
//                                catsView.connectionError(it.message + 2)
//                            }
//                        }
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