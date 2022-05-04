package otus.homework.reactivecats

import android.content.Context

class DiContainer {

    fun localRepository(context: Context) = LocalRoomRepository()
}
