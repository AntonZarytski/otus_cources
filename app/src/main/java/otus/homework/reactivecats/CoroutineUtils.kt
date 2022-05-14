package otus.homework.reactivecats

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val ioThread: CoroutineScope = CoroutineScope(Dispatchers.IO)
private val mainThread: CoroutineScope = CoroutineScope(Dispatchers.Main)

fun ioThread(command: () -> Unit ) {
    ioThread.launch {
        command.invoke()
    }
}

fun mainThread(command: () -> Unit ) {
    mainThread.launch {
        command.invoke()
    }
}