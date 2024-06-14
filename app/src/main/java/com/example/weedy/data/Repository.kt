package com.example.weedy.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.entities.Nutrients
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.entities.MasterPlant

class Repository(private val database: PlantDatabase) {

    private val TAG = "Repository"

    val plantMasterList = database.plantDao.getAll()

    val nutrientList = database.plantDao.getAllNutrients()

    val soilList = database.plantDao.getAllSoilTypes()

    private var _plantList = MutableLiveData<List<Plant>>()
    val plantlist: LiveData<List<Plant>> get() = _plantList


    private fun createDisplayPlants() {
        if (plantMasterList.value != null) {
            for (masterPlant in plantMasterList.value!!) {
                val geneticID = masterPlant.geneticID
                plantlist.value?.plus(
                    Plant(
                        masterPlant = masterPlant,
                        )
                )
            }
        }
    }

    suspend fun insertPlant(plant: MasterPlant) = database.plantDao.insert(plant)

    suspend fun updatePlant(plant: MasterPlant) = database.plantDao.update(plant)

    suspend fun deletePlant(plant: MasterPlant) = database.plantDao.deletePlant(plant)

    suspend fun loadNutrientList(nutrientList: List<Nutrients>) =
        database.plantDao.insertNutrientList(nutrientList)

    suspend fun loadSoilList(soilList: List<Soil>) = database.plantDao.insertSoilList(soilList)


}
