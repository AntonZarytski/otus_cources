package otus.homework.flowcats

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException

class CatsRepository(
    private val catsService: CatsService,
    private val refreshIntervalMs: Long = 5000
) {

    fun listenForCatFacts() : Flow<Result> = flow {
        while (true) {
            try {
                val latestNews = catsService.getCatFact()
                emit(Result.Success<Fact>(latestNews))
                delay(refreshIntervalMs)
//                throw RuntimeException("Test Exception")
            }catch (throwable: Throwable) {
                emit(Result.Error(throwable))
                delay(refreshIntervalMs)
            }
        }
    }
}