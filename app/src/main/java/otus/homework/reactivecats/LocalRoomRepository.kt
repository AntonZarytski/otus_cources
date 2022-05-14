package otus.homework.reactivecats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import otus.homework.reactivecats.local_repository.UserInput
import otus.homework.reactivecats.local_repository.UserInputDataBase

class LocalRoomRepository(context: Context) {

    private val db = Room.databaseBuilder(context, UserInputDataBase::class.java, "Sample.db")
        .addMigrations(UserInputDataBase.migrationFrom1to2Version)
//        .allowMainThreadQueries()
        .build()

    fun isTextAlreadyExist(newInput: String): Boolean {
        return db.inputDao.ifExistByValue(newInput)
    }

    fun getInputList(): LiveData<List<UserInput>> {
        return db.inputDao.getUserInputs()
    }

    fun writeNewInput(newInput: String) {
        db.inputDao.insertInput(UserInput(inputValue = newInput))
    }

    fun removeInput(userInput: UserInput) {
        db.inputDao.delete(userInput)
    }
}