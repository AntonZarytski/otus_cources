package otus.homework.reactivecats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CatsViewModel(
    val catsService: CatsService,
    val localCatFactsGenerator: LocalCatFactsGenerator,
    context: Context
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData
    private var netDisposable: Disposable? = null
    private var localDisposable: Disposable? = null

    init {
        //for check task 2
//        netDisposable =
//            catsService.getCatFact()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    _catsLiveData.value = Success(it)
//                }, {
//                    _catsLiveData.value =
//                        Error(it.message ?: context.getString(R.string.default_error_text))
//                    throw it
//                })

        //For check task 4
//        localDisposable = localCatFactsGenerator.generateCatFactPeriodically()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                _catsLiveData.value = Success(it)
//            }, {
//                _catsLiveData.value =
//                    Error(it.message ?: context.getString(R.string.default_error_text))
//                throw it
//            })

        //For check task 5
        netDisposable = getFacts()
    }

    private fun getFacts() =
        Observable.interval(2, TimeUnit.SECONDS)
            .map {
                catsService.getCatFact()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        _catsLiveData.value = Success(it)
                    }, {
                        //task 3
                        localDisposable = localCatFactsGenerator.generateCatFact()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(Consumer {
                                _catsLiveData.value = Success(it)
                            })
                    })
            }.subscribe()

    override fun onCleared() {
        if (netDisposable != null && netDisposable?.isDisposed!!) {
            netDisposable?.dispose()
        }
        if (localDisposable != null && localDisposable?.isDisposed!!) {
            localDisposable?.dispose()
        }
        super.onCleared()
    }
}

class CatsViewModelFactory(
    private val catsRepository: CatsService,
    private val localCatFactsGenerator: LocalCatFactsGenerator,
    private val context: Context
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository, localCatFactsGenerator, context) as T
}

sealed class Result
data class Success(val fact: Fact) : Result()
data class Error(val message: String) : Result()
object ServerError : Result()