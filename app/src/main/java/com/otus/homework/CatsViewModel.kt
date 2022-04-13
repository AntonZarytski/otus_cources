package com.otus.homework

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                val fact = catsService.getCatFact()
                toMainThread {
                    factVm.value = fact
                }
//                throw RuntimeException("test")
            }
        }

        launchRequest {
            toIoThread {
                val image = catsService.getCatImage()
                toMainThread {
                    imageVm.value = image
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
                        errorVm.value = e.message
                    }
                }
            } ?: throw SocketTimeoutException("Не удалось получить ответ от сервера")
        }
    }
}