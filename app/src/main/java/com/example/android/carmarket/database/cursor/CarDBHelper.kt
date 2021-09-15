package com.example.android.carmarket.database.cursor

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.android.carmarket.database.*
import com.example.android.carmarket.model.Car


class CarDBHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $DB_NAME(" +
                    "$BRAND $TYPE_TEXT, " +
                    "$INFO $TYPE_TEXT, " +
                    "$CATEGORY $TYPE_TEXT, " +
                    "$KM $TYPE_REAL, " +
                    "$PRICE $TYPE_REAL, " +
                    "$ID $TYPE_INTEGER PRIMARY KEY)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DB_NAME")
        onCreate(db)
    }

    private fun getAllCars(filter: String?): Cursor {
        return if (filter == null)
            readableDatabase.rawQuery("SELECT * FROM $DB_NAME", null)
        else
            readableDatabase.rawQuery(
                "SELECT * FROM $DB_NAME WHERE $CATEGORY = '$filter'",
                null
            )
    }

    fun getCar(id: Int): Car? {
        var car: Car? = null
        val cursor = readableDatabase.rawQuery("SELECT * FROM $DB_NAME WHERE id = '$id'", null)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val brand = cursor.getString(cursor.getColumnIndexOrThrow(BRAND))
                    val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
                    val km = cursor.getDouble(cursor.getColumnIndexOrThrow(KM))
                    val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE))
                    val _id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                    car = Car(brand, info, category, km, price, _id)
                } while (cursor.moveToNext())
            }
        }
        return car
    }

    fun getCarsList(filter: String?): List<Car> {
        val cars = mutableListOf<Car>()
        val cursor = getAllCars(filter)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val brand = cursor.getString(cursor.getColumnIndexOrThrow(BRAND))
                    val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
                    val km = cursor.getDouble(cursor.getColumnIndexOrThrow(KM))
                    val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE))
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                    cars.add(Car(brand, info, category, km, price, id))
                } while (cursor.moveToNext())
            }
        }
        return cars
    }


}