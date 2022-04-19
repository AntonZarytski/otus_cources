package otus.homework.flow

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test


@ExperimentalCoroutinesApi
class SampleInteractorTest {

    private val dotsRepository = mockk<SampleRepository>(relaxed = true)
    private val dotsInteractor = SampleInteractor(dotsRepository)

    @Test
    fun `test task1`() = runBlockingTest {
        every { dotsRepository.produceNumbers() } returns flowOf(7, 12, 4, 8, 11, 5, 7, 16, 99, 1)

        val expected = listOf("35 won", "55 won", "25 won")
        val actual = dotsInteractor.task1()
        actual.collect(){
            println(it)
        }

        assertEquals(expected, actual.toList())
    }

    @Test
    fun `test task2`() = runBlockingTest {
        every { dotsRepository.produceNumbers() } returns (1..21).asFlow()

        val expected = listOf(
            "1",
            "2",
            "3",
            "Fizz",
            "4",
            "5",
            "Buzz",
            "6",
            "Fizz",
            "7",
            "8",
            "9",
            "Fizz",
            "10",
            "Buzz",
            "11",
            "12",
            "Fizz",
            "13",
            "14",
            "15",
            "FizzBuzz",
            "16",
            "17",
            "18",
            "Fizz",
            "19",
            "20",
            "Buzz",
            "21",
            "Fizz"
        )
        val actual = dotsInteractor.task2()
        dotsInteractor.task2().collect{
            println(it)
        }

        assertEquals(expected, actual.toList())
    }

    @Test
    fun `test task3`() = runBlockingTest {
        every { dotsRepository.produceColors() } returns flowOf(
            "Red",
            "Green",
            "Blue",
            "Black",
            "White"
        )
        every { dotsRepository.produceForms() } returns flowOf("Circle", "Square", "Triangle")

        val expected = listOf("Red" to "Circle", "Green" to "Square", "Blue" to "Triangle")
        val actual = dotsInteractor.task3().toList()

        assertEquals(expected, actual)
    }

    @Test
    fun `test task4`() = runBlockingTest {
        every { dotsRepository.produceNumbers() } returns flow {
            (1..10).forEach {
                if (it == 5) {
                    throw IllegalArgumentException("Failed")
                } else {
                    emit(it)
                }
            }
        }

        val expected = listOf(1, 2, 3, 4, -1)
        val actual = dotsInteractor.task4().toList()

        assertEquals(actual, expected)

        verify(exactly = 1) { dotsRepository.completed() }
    }

    @Test
    fun `test task4 negative`() = runBlockingTest {
        every { dotsRepository.produceNumbers() } returns flow {
            (1..10).forEach {
                if (it == 5) {
                    throw SecurityException("Security breach")
                } else {
                    emit(it)
                }
            }
        }

        assertThrows(SecurityException::class.java){
            runBlockingTest {
                dotsInteractor.task4().toList()
            }

        }
        verify(exactly = 1) { dotsRepository.completed() }
    }
}