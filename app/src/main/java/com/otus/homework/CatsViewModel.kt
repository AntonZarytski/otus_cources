package com.otus.homework

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otus.homework.networkUtils.NetworkResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    private val timeoutMills: Long = 10000L

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            when (exception) {
                is SocketTimeoutException -> errorLd.value = exception.message
                else -> Log.e("exceptionHandler", ": ", exception)
            }
        }
    }

    val factLd = MutableLiveData<Fact>()
    val imageLd = MutableLiveData<Image>()
    val errorLd = MutableLiveData<String>()

    fun onInitComplete() {
        launchRequest {
            toIoThread {
                val result = catsService.getCatFact()
                toMainThread {
                    when (result) {
                        is NetworkResult.Success<Fact> -> {
                            factLd.value = result.body
                        }
                        is NetworkResult.Fail -> {
                            errorLd.value = result.error.message
                        }
                    }
                }
//                throw RuntimeException("test")
            }
        }

        launchRequest {
            toIoThread {
                val result = catsService.getCatImage()
                toMainThread {
                    when (result) {
                        is NetworkResult.Success<Image> -> {
                            imageLd.value = result.body
                        }
                        is NetworkResult.Fail -> {
                            errorLd.value = result.error.message
                        }
                    }
                }
//                throw RuntimeException("test")
            }
        }
    }

    private fun launchRequest(command: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler + SupervisorJob()) {
            withTimeoutOrNull(timeoutMills) {
                try {
                    command.invoke()
                } catch (e: Exception) {
                    CrashMonitor.trackWarning(e)
                    toMainThread {
                        errorLd.value = e.message
                    }
                }
            } ?: throw SocketTimeoutException("Не удалось получить ответ от сервера")
        }
    }
}