package com.example.weedy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.entities.Nutrients
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.models.actions.RepotAction
import com.example.weedy.data.models.record.GrowthStateRecord

class AppRepository(private val database: PlantDatabase) {

    private val TAG = "Repository"

    val plantMasterList = database.plantDao.getAll()

    val nutrientList = database.plantDao.getAllNutrients()

    val soilList = database.plantDao.getAllSoilTypes()


    suspend fun insertGrowthState(state: GrowthStateRecord) = database.plantDao.insertGrowthStateRecord(state)
    suspend fun insertRepotRecord(repot: RepotAction) = database.plantDao.insertRepot(repot)
    suspend fun getPlantByID(searchID: Long): MasterPlant = database.plantDao.getPlantByID(searchID)
    suspend fun insertPlant(plant: MasterPlant): Long = database.plantDao.insert(plant)
    suspend fun updatePlant(plant: MasterPlant) = database.plantDao.update(plant)
    suspend fun deletePlant(plant: MasterPlant) = database.plantDao.deletePlant(plant)
    suspend fun loadNutrientList(nutrientList: List<Nutrients>) = database.plantDao.insertNutrientList(nutrientList)
    suspend fun loadSoilList(soilList: List<Soil>) = database.plantDao.insertSoilList(soilList)
}
