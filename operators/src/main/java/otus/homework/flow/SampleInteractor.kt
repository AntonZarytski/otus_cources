package otus.homework.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class SampleInteractor(
    private val sampleRepository: SampleRepository
) {

    /**
     * Реализуйте функцию task1 которая последовательно:
     * 1) возводит числа в 5ую степень
     * 2) убирает чила <= 20
     * 3) убирает четные числа
     * 4) добавляет постфикс "won"
     * 5) берет 3 первых числа
     * 6) возвращает результат
     */
    //TODO в условие ошибка, <= 20 после возведения в ^5 невозможно с данными числами
    fun task1(): Flow<String> {
        return sampleRepository.produceNumbers()
            .map {
                Math.pow(it.toDouble(), 5.0)
            }.filter {
                it <= 20
            }.filter {
                it.toInt() % 2 != 0
            }.map {
                "$it won"
            }.take(3).transform {
                emit(it)
            }
    }

    /**
     * Классическая задача FizzBuzz с небольшим изменением.
     * Если входное число делится на 3 - эмитим само число и после него эмитим строку Fizz
     * Если входное число делится на 5 - эмитим само число и после него эмитим строку Buzz
     * Если входное число делится на 15 - эмитим само число и после него эмитим строку FizzBuzz
     * Если число не делится на 3,5,15 - эмитим само число
     */
    //TODO в условие ошибка, FizzBuzz не может быть так как 15 будет делится на 3 и на 5
    fun task2(): Flow<String> {
        return sampleRepository.produceNumbers()
            //TODO почему при применении .transform несколько раз, вместо when, я получаю пустой лист
            .transform<Int, String> {
                it.toString()
                when {
                    it % 3 == 0 -> {
                        emit(it.toString())
                        emit("Fizz")
                    }
                    it % 5 == 0 -> {
                        emit(it.toString())
                        emit("Buzz")
                    }
                    it % 15 == 0 -> {
                        emit(it.toString())
                        emit("FizzBuzz")
                    }
                    else -> {
                        emit(it.toString())
                    }
                }
            }
    }

    /**
     * Реализуйте функцию task3, которая объединяет эмиты из двух flow и возвращает кортеж Pair<String,String>(f1,f2),
     * где f1 айтем из первого флоу, f2 айтем из второго флоу.
     * Если айтемы в одно из флоу кончились то результирующий флоу также должен закончится
     */
    fun task3(): Flow<Pair<String, String>> {
        return sampleRepository.produceColors()
            .zip(sampleRepository.produceForms()) { a, b ->
                Pair(a, b)
            }
    }

    /**
     * Реализайте функцию task4, которая обрабатывает SecurityException и в качестве фоллбека
     * эмитит число -1.
     * Если тип эксепшена != SecurityException, пробросьте его дальше
     * При любом исходе, будь то выброс исключения или успешная отработка функции вызовите метод dotsRepository.completed()
     */
    fun task4(): Flow<Int> {
        val flow = sampleRepository.produceNumbers()
        return try {
            flow.catch { e: Throwable ->
                when (e) {
                    is SecurityException -> {
                        emit(-1)
                    }
                }
                sampleRepository.completed()
                throw e
            }
        } catch (exception: Throwable) {
            return flow.transform {
                emit(-1)
            }
        }
    }
}