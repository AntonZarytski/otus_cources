package otus.homework.reactivecats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InputFactViewModel(
    private val localRepository: LocalRoomRepository,
) : ViewModel() {

    private val _inputedFacts = MutableLiveData<MutableList<Fact>>()
    val inputedFacts: LiveData<MutableList<Fact>> = _inputedFacts

    private val _inputError = MutableLiveData<String>()
    val inputError: LiveData<String> = _inputError

    init {
        _inputedFacts.value = localRepository.getInputList()
    }

    fun addNewInput(newInput: String) {
        if (newInput.isBlank()){
            _inputError.value = "text cannot be empty"
           return
        }
        if (localRepository.isTextAlreadyExist(newInput)){
            _inputError.value = "this text was already added"
            return
        }
        localRepository.writeNewInput(newInput)
        _inputedFacts.value = localRepository.getInputList()
    }
}

class CatsViewModelFactory(
    private val localCatFactsGenerator: LocalRoomRepository,
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputFactViewModel(localCatFactsGenerator) as T
    }
}