package com.example.android.carmarket.database.room

import androidx.room.*
import com.example.android.carmarket.database.DB_NAME
import com.example.android.carmarket.model.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg car: Car)

    @Update
    suspend fun update(car: Car)

    @Query("SELECT * FROM $DB_NAME WHERE id = :id ")
    fun getCarById(id: Int): Flow<Car>

    @Query("SELECT * FROM $DB_NAME WHERE category = :category")
    fun filterByCategory(category: String): Flow<List<Car>>

    @Delete
    suspend fun delete(car: Car)

    @Query("DELETE FROM $DB_NAME")
    suspend fun clear()

    @Query("SELECT * FROM $DB_NAME")
    fun selectAll(): Flow<List<Car>>
}