package com.example.android.carmarket.view.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.carmarket.database.CarRepository

class AddFactory (
    private val repository: CarRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(repository ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}