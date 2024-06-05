package com.example.weedy.data

import android.util.Log
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.entities.Plant
import com.example.weedy.data.offlineData.ExampleData

class Repository(private val database: PlantDatabase) {

    private val TAG = "Repository"

    val plantList = database.plantDao.getAll()

    suspend fun insertList(plantList: List<Plant>) = database.plantDao.insertList(plantList)

    suspend fun insert(plant: Plant) = database.plantDao.insert(plant)

    suspend fun update(plant: Plant) = database.plantDao.update(plant)

    suspend fun delete(plant: Plant) = database.plantDao.deletePlant(plant)
    suspend fun checkLoadingExampleData() {
            if (database.plantDao.getPlantListSize() == 0) {
                database.plantDao.insertList(ExampleData.loadExamplePlants())
                Log.d(TAG,"Example data Loaded")
            }
    }
}