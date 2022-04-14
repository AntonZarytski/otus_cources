package com.otus.homework

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otus.homework.networkUtils.NetworkResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            when (exception) {
                is SocketTimeoutException -> errorLd.value = "Не удалось получить ответ от серверо"
                else -> Log.e("exceptionHandler", ": ", exception)
            }
        }
    }

    val factLd = MutableLiveData<Fact>()
    val imageLd = MutableLiveData<Image>()
    val errorLd = MutableLiveData<String>()

    fun onInitComplete() {
        launchRequest {
            when (val result = catsService.getCatFact()) {
                is NetworkResult.Success<Fact> -> {
                    factLd.value = result.body
                }
                is NetworkResult.Fail -> {
                    errorLd.value = result.error.message
                }
            }
            throw RuntimeException("test")
        }

        launchRequest {
            when (val result = catsService.getCatImage()) {
                is NetworkResult.Success<Image> -> {
                    imageLd.value = result.body
                }
                is NetworkResult.Fail -> {
                    errorLd.value = result.error.message
                }
            }
//            throw RuntimeException("test")
        }
    }

    private fun launchRequest(command: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler + SupervisorJob()) {
            try {
                command.invoke()
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                toMainThread {
                    errorLd.value = e.message
                }
            }
        }
    }
}