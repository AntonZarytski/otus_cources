package otus.homework.reactivecats.local_repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserInputs")
data class UserInput(
    //id будет всегда автоинкрементится
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var inputId: Int = 0,
    @ColumnInfo(name = "input")
    val inputValue: String
) {
    override fun equals(other: Any?): Boolean {
        if (other is UserInput) {
            return other.inputValue == inputValue && other.inputId == inputId
        }
        return false
    }

    override fun hashCode(): Int {
        return (inputValue + inputId).hashCode()
    }
}
