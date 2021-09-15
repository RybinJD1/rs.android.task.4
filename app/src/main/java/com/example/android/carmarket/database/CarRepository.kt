package com.example.android.carmarket.database

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.preference.PreferenceManager
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.android.carmarket.database.cursor.CarDBHelper
import com.example.android.carmarket.database.room.CarDao
import com.example.android.carmarket.model.Car
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class CarRepository(
    private val carDao: CarDao,
    private val application: Application,
    private val dbHelper: CarDBHelper = CarDBHelper(application)
) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val management = preferences.getString("list_management", ROOM).toString().trim()
    private val asSort = preferences.getBoolean("sort", false)
    private val sort = preferences.getString("list_sort", ID).toString().trim()
    private val filter = preferences.getString("category", NONE).toString().trim()
    private val asFilter = preferences.getBoolean("filter", false)

    fun allCars(): Flow<List<Car>> {
        return if (management == ROOM) {
            if (asFilter) {
                carDao.filterByCategory(filter).map { sortListCars(it) }
            } else
                carDao.selectAll().map { sortListCars(it) }
        } else {
            if (asFilter) {
                flowOf(sortListCars(dbHelper.getCarsList(filter)))
            } else
                flowOf(sortListCars(dbHelper.getCarsList(null)))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: Car) {
        if (management == ROOM)
            carDao.insert(car)
        else {
            val contentValues = ContentValues()
            contentValues.put(BRAND, car.brand)
            contentValues.put(INFO, car.info)
            contentValues.put(CATEGORY, car.category)
            contentValues.put(KM, car.km)
            contentValues.put(PRICE, car.price)
            dbHelper.readableDatabase.insert(DB_NAME, null, contentValues)
        }
    }

    fun getCar(id: Int): Flow<Car?> {
        return if (management == ROOM) carDao.getCarById(id)
        else flowOf(dbHelper.getCar(id))
    }

    suspend fun deleteCar(car: Car) {
        if (management == ROOM) carDao.delete(car)
        else {
            val db: SQLiteDatabase = application.applicationContext.openOrCreateDatabase(
                DB_NAME, Context.MODE_PRIVATE, null
            )
            db.use { db.execSQL("DELETE FROM $DB_NAME WHERE $ID = '${car.id}'") }
        }
    }

    suspend fun updateCar(car: Car) {
        if (management == ROOM)
            carDao.update(car)
        else {
            val db: SQLiteDatabase = application.applicationContext.openOrCreateDatabase(
                DB_NAME,
                Context.MODE_PRIVATE,
                null
            )
            db.use {
                db.execSQL(
                    "UPDATE $DB_NAME SET $BRAND = '${car.brand}', $INFO = '${car.info}', $CATEGORY = '${car.category}', $KM = '${car.km}', $PRICE = '${car.price}' WHERE $ID = '${car.id}'"
                )
            }
        }
    }

    private fun sortListCars(list: List<Car>): List<Car> {
        return if (asSort) {
            when (sort) {
                BRAND -> list.sortedBy { it.brand }
                CATEGORY -> list.sortedBy { it.category }
                KM -> list.sortedBy { it.km }
                PRICE -> list.sortedBy { it.price }
                else -> list.sortedBy { it.id }
            }
        } else {
            when (sort) {
                BRAND -> list.sortedBy { it.brand }
                CATEGORY -> list.sortedBy { it.category }
                KM -> list.sortedBy { it.km }
                PRICE -> list.sortedBy { it.price }
                else -> list.sortedBy { it.id }
            }
        }
    }


}