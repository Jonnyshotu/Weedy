package com.example.weedy

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.AppRepository
import com.example.weedy.data.StrainRepository
import com.example.weedy.data.local.getDatabase
import com.example.weedy.data.local.offlineData.NutrientsProducts
import com.example.weedy.data.local.offlineData.SoilProducts
import com.example.weedy.data.models.actions.PlantedAction
import com.example.weedy.data.models.actions.RepotAction
import com.example.weedy.data.models.record.GrowthStateRecord
import com.example.weedy.data.models.record.HealthRecord
import com.example.weedy.data.models.record.ImagesRecord
import com.example.weedy.data.models.record.LightRecord
import com.example.weedy.data.models.record.MeasurementsRecord
import com.example.weedy.data.models.record.NutrientsRecord
import com.example.weedy.data.models.record.RepellentsRecord
import com.example.weedy.data.models.record.TrainingRecord
import com.example.weedy.data.models.record.WateringRecord
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "ViewModel"


    private val database = getDatabase(application)
    private val appRepository = AppRepository(database)
    private val strainRepository = StrainRepository(database, application.applicationContext)

    private var _plant = MutableLiveData<MasterPlant>()
    val plant: LiveData<MasterPlant> get() = _plant

    val plantList = appRepository.plantMasterList
    val localGeneticCollection = strainRepository.localGeneticCollection
    val remoteGeneticCollection = strainRepository.remoteGeneticCollection
    val nutrientsList = appRepository.nutrientList
    val soilList = appRepository.soilList

    init {
        loadLocalGenetics()
        loadSoilTypes()
        loadNutrients()
    }

    //region coroutines

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

    fun insertPlant(plant: MasterPlant, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = appRepository.insertPlant(plant)
            onResult(id)
            Log.d(TAG, "Inserted plant ID: $id")
        }
    }

    fun updatePlant(plant: MasterPlant) {
        viewModelScope.launch {
            appRepository.updatePlant(plant)
        }
    }

    fun getPlantByID(searchID: Long) {
        viewModelScope.launch {
            val plant = appRepository.getPlantByID(searchID)
            _plant.value = plant
        }
    }

    fun deletePlant(plant: MasterPlant) {
        viewModelScope.launch {
            appRepository.deletePlant(plant)
        }
    }

    fun insertRepot(repot: RepotAction) {
        viewModelScope.launch {
            appRepository.insertRepot(repot)
        }
    }

    fun insertPlanted(planted: PlantedAction) {
        viewModelScope.launch {
            appRepository.insertPlanted(planted)
        }
    }

    fun insertGrowthState(state: GrowthStateRecord) {
        viewModelScope.launch {
            appRepository.insertGrowthState(state)
        }
    }

    fun insertHealth(health: HealthRecord) {
        viewModelScope.launch {
            appRepository.insertHealth(health)
        }
    }

    fun insertImage(images: ImagesRecord) {
        viewModelScope.launch {
            appRepository.insertImage(images)
        }
    }

    fun insertLight(light: LightRecord) {
        viewModelScope.launch {
            appRepository.insertLight(light)
        }
    }

    fun insertMeasurement(measurement: MeasurementsRecord) {
        viewModelScope.launch {
            appRepository.insertMeasurement(measurement)
        }
    }

    fun insertNutrient(nutrients: NutrientsRecord) {
        viewModelScope.launch {
            appRepository.insertNutrient(nutrients)
        }
    }

    fun insertRepellent(repellent: RepellentsRecord) {
        viewModelScope.launch {
            appRepository.insertRepellent(repellent)
        }
    }

    fun insertTraining(training: TrainingRecord) {
        viewModelScope.launch {
            appRepository.insertTraining(training)
        }
    }

    fun insertWatering(watering: WateringRecord) {
        viewModelScope.launch {
            appRepository.insertWatering(watering)
        }
    }

    fun getWateringRecordByPlantID(
        plantID: Long,
    ): LiveData<List<WateringRecord>> {
        return appRepository.getWateringRecordByPlantID(plantID)
    }


    //endregion
}

