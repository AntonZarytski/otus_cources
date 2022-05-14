package otus.homework.reactivecats.local_repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [UserInput::class], version = 1)
abstract class UserInputDataBase() : RoomDatabase() {

    abstract val inputDao: UserInputDao

    companion object {
        val migrationFrom1to2Version: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Поскольку мы не изменяли таблицу, здесь больше ничего не нужно делать.
            }
        }
    }
}