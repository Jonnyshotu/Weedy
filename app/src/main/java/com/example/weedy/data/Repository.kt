package com.example.weedy.data

import ExampleData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.entities.Nutrients
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.entities.Genetic
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.remote.StrainApi

class Repository(private val database: PlantDatabase) {

    private val TAG = "Repository"



    val plantMasterList = database.plantDao.getAll()

    val geneticList = database.plantDao.getAllGenetics()

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
                        genetic = geneticList.value?.find { it.id == geneticID },

                        )
                )
            }
        }
    }


    private suspend fun insertPlantList(plantList: List<MasterPlant>) =
        database.plantDao.insertList(plantList)

    suspend fun insertPlant(plant: MasterPlant) = database.plantDao.insert(plant)

    suspend fun updatePlant(plant: MasterPlant) = database.plantDao.update(plant)

    suspend fun deletePlant(plant: MasterPlant) = database.plantDao.deletePlant(plant)
    suspend fun checkLoadingExampleData() {
        if (database.plantDao.getPlantListSize() == 0) {
            insertPlantList(ExampleData.loadExampleMasterPlants())
            Log.d(TAG, "Example data Loaded")
        }
    }

    suspend fun loadNutrientList(nutrientList: List<Nutrients>) =
        database.plantDao.insertNutrientList(nutrientList)

    suspend fun loadSoilList(soilList: List<Soil>) = database.plantDao.insertSoilList(soilList)


}
