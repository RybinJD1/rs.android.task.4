package com.example.android.carmarket

import com.example.android.carmarket.model.Car


fun categories(): List<String> {
    return listOf("SUVs", "Trucks", "Crossovers", "Sedans", "Coupes", "Sports Cars", "Luxury",
        "Convertibles", "Electric Vehicles", "Other")
}

fun startCar() : List<Car> {
    return (0..10)
        .map { Car("brand$it", "info", categories()[(categories().indices).random()],
            (0..100000).random().toDouble(), (0..999999).random().toDouble()) }
        .toList()
}