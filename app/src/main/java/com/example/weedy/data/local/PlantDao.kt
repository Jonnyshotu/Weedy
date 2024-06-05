package com.example.weedy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weedy.data.entities.Plant


@Dao
interface PlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: Plant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(plantList: List<Plant>)

    @Update
    suspend fun update(plant: Plant)

    @Query("SELECT * FROM Plant")
    fun getAll(): LiveData<List<Plant>>

    @Query("SELECT COUNT(*) FROM Plant")
    suspend fun getPlantListSize(): Int

    @Delete
    suspend fun deletePlant(plant: Plant)
}