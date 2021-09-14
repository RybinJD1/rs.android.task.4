package com.example.android.carmarket.database.room

import androidx.room.*
import com.example.android.carmarket.database.CATEGORY
import com.example.android.carmarket.database.DB_NAME
import com.example.android.carmarket.model.Car
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg car: Car)

    @Update
    suspend fun update(car: Car)

    @Query("SELECT * FROM $DB_NAME WHERE id = :id ")
    fun getCarForDB(id: Int): Flow<Car>

    fun getDogDistinctUntilChanged(id: Int) =
        getCarForDB(id).distinctUntilChanged()


    @Query("SELECT * FROM $DB_NAME WHERE $CATEGORY = :category")
    fun getFilter(category: String): Flow<List<Car>>

    @Delete
    suspend fun delete(car: Car)

    @Query("DELETE FROM $DB_NAME")
    suspend fun clear()

    @Query("SELECT * FROM $DB_NAME")
    fun getAllElements(): Flow<List<Car>>
}