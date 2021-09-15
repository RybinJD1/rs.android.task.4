package com.example.android.carmarket.view.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.carmarket.database.CarRepository

class ListFactory (
    private val repository: CarRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(repository ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}