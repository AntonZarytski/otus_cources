package com.otus.homework

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun mainThread(command: () -> Unit) {
    withContext(Dispatchers.Main) {
        command.invoke()
    }
}

suspend fun ioThread(command: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
        command.invoke()
    }
}