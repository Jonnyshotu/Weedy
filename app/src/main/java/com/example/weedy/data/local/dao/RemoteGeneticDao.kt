package com.example.weedy.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.entities.RemoteGenetic

@Dao
interface RemoteGeneticDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenetic(remoteGenetic: RemoteGenetic)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGeneticList(remoteGenetic: List<RemoteGenetic>)

    @Query("SELECT * FROM remote_genetic_table")
    fun getAllGenetics(): LiveData<List<RemoteGenetic>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGenetic(localGenetic: LocalGenetic)
}