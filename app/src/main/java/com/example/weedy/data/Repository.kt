package com.example.weedy.data

import ExampleData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.entities.Plant
import com.example.weedy.data.models.Nutrients
import com.example.weedy.data.models.Soil
import com.example.weedy.data.local.offlineData.NutrientsProducts
import com.example.weedy.data.local.offlineData.SoilProducts

class Repository(private val database: PlantDatabase) {

    private val TAG = "Repository"

    val plantList = database.plantDao.getAll()

    private var _nutrientList = MutableLiveData<List<Nutrients>>()
    val nutrientList: LiveData<List<Nutrients>> get() = _nutrientList


    private var _soilList = MutableLiveData<List<Soil>>()
    val soilList: LiveData<List<Soil>> get() = _soilList




    suspend fun insertPlantList(plantList: List<Plant>) = database.plantDao.insertList(plantList)

    suspend fun insertPlant(plant: Plant) = database.plantDao.insert(plant)

    suspend fun updatePlant(plant: Plant) = database.plantDao.update(plant)

    suspend fun deletePlant(plant: Plant) = database.plantDao.deletePlant(plant)
    suspend fun checkLoadingExampleData() {
        if (database.plantDao.getPlantListSize() == 0) {
            insertPlantList(ExampleData.loadExamplePlants())
            Log.d(TAG, "Example data Loaded")
        }
    }
    suspend fun loadNutrientList(nutrientList: List<Nutrients>) = database.plantDao.insertNutrientList(nutrientList)

    suspend fun loadSoilList(soilList: List<Soil>) = database.plantDao.insertSoilList(soilList)

}
