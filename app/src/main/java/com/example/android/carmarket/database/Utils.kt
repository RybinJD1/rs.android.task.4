package com.example.android.carmarket

import com.example.android.carmarket.model.Car


fun categories(): List<String> {
    return listOf("suvs", "trucks", "crossovers", "sedans", "coupes", "sports cars", "luxury",
        "convertibles", "electric vehicles", "other")
}

fun startCar() : List<Car> {
    return (0..10)
        .map { Car("brand$it", "info", categories()[(categories().indices).random()],
            (0..999999).random().toDouble(), (0..9999).random().toDouble()) }
        .toList()
}