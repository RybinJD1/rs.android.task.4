package com.example.android.carmarket.view.list

import androidx.lifecycle.*
import com.example.android.carmarket.database.CarRepository
import com.example.android.carmarket.model.Car
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow as Flow


class ListViewModel(private val repository: CarRepository) : ViewModel() {

    private var _cars: Flow<List<Car>> = repository.allCars()
    val cars: Flow<List<Car>>
        get() = _cars

    private var _cursorCars = flowOf<List<Car>>()
    val cursorCars: Flow<List<Car>>
        get() = _cursorCars

    private var _car = flowOf<Car?>()
    val car: Flow<Car?>
        get() = _car

    fun getCar(id: Int) {
        viewModelScope.launch {
            _car = getCarForDB(id)
        }
    }
    fun updateCursorCars() {
        getCursorCars()
    }

    private fun getCursorCars() {
        viewModelScope.launch {
            _cursorCars = repository.allCars()
        }

    }

    private fun getCarForDB(id: Int): Flow<Car?> {
        return repository.getCar(id)
    }

    fun deleteCar(car: Car) = viewModelScope.launch {
        repository.deleteCar(car)
    }

}
