package otus.homework.reactivecats

data class Fact(
    val text: String


) {
    override fun equals(other: Any?): Boolean {
        if (other is Fact) {
            return other.text == text
        }
        return false
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }
}