package otus.homework.reactivecats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CatsViewModel(
    context: Context,
    private val catsService: CatsService,
    private val localCatFactsGenerator: LocalCatFactsGenerator,
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData
    private var disposable: CompositeDisposable = CompositeDisposable()

    init {
        //for check task 2
//        disposable.add(
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
//        )

        //For check task 4
//        disposable.add(
//            localCatFactsGenerator.generateCatFactPeriodically()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    _catsLiveData.value = Success(it)
//                }, {
//                    _catsLiveData.value =
//                        Error(it.message ?: context.getString(R.string.default_error_text))
//                    throw it
//                })
//        )
        //For check task 5
        disposable.add(getFacts())
    }

    private fun getFacts() =
        Observable.interval(2, TimeUnit.SECONDS)
            .flatMapSingle {
                catsService.getCatFact()
                    .onErrorResumeNext {
                        localCatFactsGenerator.generateCatFact()
                    }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _catsLiveData.value = Success(it)
            }

    override fun onCleared() {
        if (disposable.isDisposed) {
            disposable.dispose()
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
        CatsViewModel(context, catsRepository, localCatFactsGenerator) as T
}

sealed class Result
data class Success(val fact: Fact) : Result()
data class Error(val message: String) : Result()
object ServerError : Result()