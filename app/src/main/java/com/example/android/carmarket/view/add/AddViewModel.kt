package com.example.android.carmarket.view.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.carmarket.database.CarRepository
import com.example.android.carmarket.model.Car
import kotlinx.coroutines.launch

class AddViewModel(private val repository: CarRepository) : ViewModel() {

    var brand: String = "Unknown"
    var info: String = "Unknown"
    var category: String? = null
    var km: Double? = 0.0
    var price: Double? = 0.0

    fun addCar(car: Car) = viewModelScope.launch {
        repository.insert(car)
    }

    fun updateCar(car: Car) = viewModelScope.launch {
        repository.updateCar(car)
    }

}