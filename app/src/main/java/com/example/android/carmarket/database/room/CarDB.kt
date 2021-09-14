package com.example.android.carmarket.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.carmarket.database.DB_NAME
import com.example.android.carmarket.database.DB_VERSION
import com.example.android.carmarket.model.Car
import com.example.android.carmarket.startCar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Car::class], version = DB_VERSION, exportSchema = false)
abstract class CarDB : RoomDatabase() {

    abstract fun carDAO(): CarDao

    companion object {
        @Volatile
        private var INSTANCE: CarDB? = null
        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): CarDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CarDB::class.java,
                        DB_NAME
                    )
                        .addCallback(CarStartDBCallback(scope))
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private class CarStartDBCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val carDAO = database.carDAO()
                    carDAO.clear()
                    val cars = startCar()
                    cars.forEach { carDAO.insert(it) }
                }
            }
        }
    }
}


