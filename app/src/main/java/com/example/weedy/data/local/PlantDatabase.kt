package com.example.weedy.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weedy.data.entities.Plant

@Database(entities = [Plant::class], version = 1)
abstract class PlantDatabase : RoomDatabase() {


    abstract val plantDao: PlantDao
}

private val TAG = "PlantDatabase"

private lateinit var dbInstance: PlantDatabase

fun getDatabase(context: Context): PlantDatabase {
    synchronized(PlantDatabase::class.java) {
        if (!::dbInstance.isInitialized) {

            dbInstance = Room.databaseBuilder(
                context.applicationContext,
                PlantDatabase::class.java,
                "Plant_Database"
            ).build()
        }
    }
    Log.d(TAG, " Database initialized")
    return dbInstance
}