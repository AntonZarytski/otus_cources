package otus.homework.reactivecats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.reactivecats.local_repository.UserInput

class InputFactViewModel(
    private val localRepository: LocalRoomRepository,
) : ViewModel() {

    //из-за того, что Room привязан к livaData, то данные будут обновляться сами при insert/remove/update
    private val _inputedFacts by lazy { localRepository.getInputList() }
    val inputedFacts: LiveData<List<UserInput>> = _inputedFacts

    private var _inputError = MutableLiveData<String>()
    val inputError: LiveData<String> = _inputError

    fun addNewInput(newInput: String) {
        if (newInput.isBlank()) {
            _inputError.value = "text cannot be empty"
            return
        }
        ioThread {
            if (localRepository.isTextAlreadyExist(newInput)) {
                //обновлять view только в майнтреде
                mainThread { _inputError.value = "this text was already added" }
                return@ioThread
            }
            localRepository.writeNewInput(newInput)
        }
    }

    fun removeInput(userInput: UserInput) {
        ioThread {
            // удаление из бд
            localRepository.removeInput(userInput)
        }
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