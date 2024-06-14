package com.example.weedy.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weedy.data.entities.LocalGenetic

@Dao
interface LocalGeneticDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenetic(localGenetic: LocalGenetic)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGeneticList(localGeneticList: List<LocalGenetic>)

    @Query("SELECT * FROM local_genetic_table")
    fun getAllGenetics(): LiveData<List<LocalGenetic>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGenetic(localGenetic: LocalGenetic)
}