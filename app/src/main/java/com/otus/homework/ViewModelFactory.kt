package com.otus.homework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory constructor(private val service:CatsService): ViewModelProvider.Factory {

     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            CatsViewModel(this.service) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}