package com.example.weedy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.AppRepository
import com.example.weedy.data.StrainRepository
import com.example.weedy.data.local.getDatabase
import com.example.weedy.data.local.offlineData.NutrientsProducts
import com.example.weedy.data.local.offlineData.SoilProducts
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "SharedViewModel"

    var navigatePlantID: Long = 0

    private val database = getDatabase(application)
    private val appRepository = AppRepository(database)
    private val strainRepository = StrainRepository(database, application.applicationContext)

    val plantList = appRepository.plantlist
    val localGeneticCollection = strainRepository.localGeneticCollection
    val remoteGeneticCollection = strainRepository.remoteGeneticCollection
    val nutrientsList = appRepository.nutrientList
    val soilList = appRepository.soilList

    init {
   //     loadRemoteenetics()
        loadLocalGenetics()
        loadSoilTypes()
        loadNutrients()
    }

    //region coroutines

    fun loadLocalGenetics() {
        viewModelScope.launch {
            strainRepository.getLocalStrains()
        }
    }

    fun loadRemoteenetics() {
        viewModelScope.launch {
            strainRepository.getRemoteStrains()
        }
    }

    fun insertPlant(plant: MasterPlant) {
        viewModelScope.launch {
            appRepository.insertPlant(plant)
        }
    }

    fun updatePlant(plant: MasterPlant) {
        viewModelScope.launch {
            appRepository.updatePlant(plant)
        }
    }

    fun deletePlant(plant: MasterPlant) {
        viewModelScope.launch {
            appRepository.deletePlant(plant)
        }
    }

    private fun loadNutrients() {
        viewModelScope.launch {
            appRepository.loadNutrientList(NutrientsProducts.loadNutrients())
        }
    }

    private fun loadSoilTypes() {
        viewModelScope.launch {
            appRepository.loadSoilList(SoilProducts.loadSoilTypes())
        }
    }


    //endregion

//    //region plant records
//    fun health(health: Int) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            plant.health.plus(HealthRecord(health, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating health")
//        Log.d(TAG, "Health updated: ${plant.toString()}")
//    }
//
//
//    fun water(water: Double, ph: Double?) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (water != null && plant != null) {
//            plant.watering.plus(WateringRecord(water, ph, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating watering")
//        Log.d(TAG, "Watering updated: ${plant.toString()}")
//    }
//
//
//    fun nutrients(nutrient: Nutrients, amount: Double) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            plant.nutrients.plus(NutrientsRecord(nutrient, amount, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating nutrients")
//        Log.d(TAG, "Nutrients updated: ${plant.toString()}")
//    }
//
//    fun reppelents(reppelent: String) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            plant.repellents.plus(RepellentsRecord(reppelent, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating repellents")
//        Log.d(TAG, "Repellents updated: ${plant.toString()}")
//    }
//
//    fun growthStateChange(growthState: Int) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            plant.growthState.plus(GrowthStateRecord(growthState, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating growth State")
//        Log.d(TAG, "Growth state updated: ${plant.toString()}")
//    }
//
//    fun training(training: String) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            plant.training.plus(TrainingRecord(training, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating training")
//        Log.d(TAG, "Training updated: ${plant.toString()}")
//    }
//
//    fun light(lightHours: Int) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            plant.light.plus(LightRecord(lightHours, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating light hours")
//        Log.d(TAG, "Light hours updated: ${plant.toString()}")
//    }
//
//    //endregion
//
//    //region action
//    fun repot(soil: Soil, potSize: Double) {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            plant.repot.plus(RepotAction(soil, potSize, LocalDate.now()))
//            updatePlant(plant)
//        } else Log.e(TAG, "Error updating repot")
//        Log.d(TAG, "Light hours updated: ${plant.toString()}")
//    }
//
//    fun markAsDead() {
//        var plant = plantList.value?.find { it.id == navigatePlantID }
//        if (plant != null) {
//            deletePlant(plant)
//        } else Log.e(TAG, "Error deleting plant")
//        Log.d(TAG, "Plant deleted")
//    }
//    //endregion
}

