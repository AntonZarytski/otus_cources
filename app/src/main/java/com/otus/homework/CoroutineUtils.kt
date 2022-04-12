package com.otus.homework

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val mainScope = CoroutineScope(Dispatchers.Main)
private val ioScope = CoroutineScope(Dispatchers.IO)

fun launchInMainThread(command: () -> Unit) {
    mainScope.launch {
        command.invoke()
    }
}

fun launchInIoThread(command: () -> Unit) {
    ioScope.launch {
        command.invoke()
    }
}

suspend fun toMainThread(command: () -> Unit) {
    withContext(Dispatchers.Main) {
        command.invoke()
    }
}

suspend fun toIoThread(command: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
        command.invoke()
    }
}