package otus.homework.reactivecats

class LocalRoomRepository {
    private val db = mutableListOf<Fact>(Fact("text1"), Fact("text3"), Fact("text3"))

    fun isTextAlreadyExist(newInput: String): Boolean {
        return db.contains(Fact(newInput))
    }

    fun getInputList(): MutableList<Fact> {
        return db
    }

    fun writeNewInput(newInput: String) {
        db.add(Fact(newInput))
    }
}