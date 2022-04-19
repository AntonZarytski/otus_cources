package otus.homework.flowcats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    val factsFLow = catsRepository.listenForCatFacts()

    init {
        viewModelScope.launch {
            factsFLow.collect {
                flow {
                    emit(it)
                }.flowOn(Dispatchers.Main)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5000L),
                        initialValue = emptyFact
                    )
            }
        }
    }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository) as T
}