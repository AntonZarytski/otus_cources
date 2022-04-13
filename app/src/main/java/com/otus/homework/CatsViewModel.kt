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
                is SocketTimeoutException -> errorVm.value = exception.message
                else -> Log.e("exceptionHandler", ": ", exception)
            }
        }
    }

    val factVm = MutableLiveData<Fact>()
    val imageVm = MutableLiveData<Image>()
    val errorVm = MutableLiveData<String>()

    fun onInitComplete() {
        launchRequest {
            toIoThread {
                val result = catsService.getCatFact()
                toMainThread {
                    when(result) {
                        is NetworkResult.Success<Fact> -> {
                            factVm.value = result.body
                        }
                        is NetworkResult.Fail -> {
                            errorVm.value = result.error.message
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
                    when(result) {
                        is NetworkResult.Success<Image> -> {
                            imageVm.value = result.body
                        }
                        is NetworkResult.Fail -> {
                            errorVm.value = result.error.message
                        }
                    }                }
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
                        errorVm.value = e.message
                    }
                }
            } ?: throw SocketTimeoutException("Не удалось получить ответ от сервера")
        }
    }
}