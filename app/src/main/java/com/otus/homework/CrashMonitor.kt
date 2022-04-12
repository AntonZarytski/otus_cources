package com.otus.homework

import android.util.Log

object CrashMonitor {
    private val TAG = CrashMonitor.javaClass.simpleName

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(throwable: Throwable) {
        Log.e(TAG, "trackWarning: ", throwable)
    }
}