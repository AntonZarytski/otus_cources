package otus.homework.reactivecats.local_repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserInputDao {

    @Query("SELECT * FROM UserInputs")
    fun getUserInputs(): LiveData<List<UserInput>>

    @Insert
    fun insertAll(inputList: List<UserInput>)

    @Insert
    fun insertInput(input: UserInput)

    @Delete
    fun delete(user: UserInput)

    @Query("SELECT * FROM UserInputs WHERE input LIKE :inputForCheck")
    fun getAllInputsWithInputContaining(inputForCheck: String?): LiveData<List<UserInput?>>

    @Query("SELECT EXISTS (SELECT 1 FROM UserInputs WHERE id = :id)")
    fun ifExistById(id: Int): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM UserInputs WHERE input = :input)")
    fun ifExistByValue(input: String): Boolean
}