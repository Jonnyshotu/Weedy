package com.example.weedy

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weedy.data.Repository
import com.example.weedy.data.local.getDatabase
import com.example.weedy.data.models.Nutrients
import com.example.weedy.data.entities.Plant
import com.example.weedy.data.models.Soil
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "ViewModel"

    var navigatePlantID: Int = 0

    private val database = getDatabase(application)
    private val repository = Repository(database)

    val plantList = repository.plantList

    private var _plants = MutableLiveData<List<Plant>>()
    val plants: LiveData<List<Plant>>
        get() = _plants

    private var _plantsHistory = MutableLiveData<List<Plant>>()
    val plantsHistory: LiveData<List<Plant>>
        get() = _plantsHistory

    private var _nutrients = MutableLiveData<List<Nutrients>>()
    val nutrients: LiveData<List<Nutrients>>
        get() = _nutrients

    private var _soilTypes = MutableLiveData<List<Soil>>()
    val soilTypes: LiveData<List<Soil>>
        get() = _soilTypes


    init {
        suspend fun exampleData(plant: Plant){
            viewModelScope.launch {
                repository.checkLoadingExampleData()
            }
        }
    }

    suspend fun insertPlant(plant: Plant){
        viewModelScope.launch {
            repository.insert(plant)
        }
    }

//TODO
    fun weeksOld(): Int {
        return (ChronoUnit.WEEKS.between(planted, LocalDate.now())).toInt()
    }

    fun weeksTilHarvest(): Int {
        return (ChronoUnit.WEEKS.between(
            planted,
            planted.plusWeeks(floweringTime.toLong())
        )).toInt()
    }


    //region treatments
    fun health(health: Int) {
        if (health != null) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(health = health)
                } else {
                    plant
                }
            }
            Log.d("$TAG health", _plants.value?.filter { it.id == navigatePlantID }.toString())
        }
    }

    fun water(water: Pair<Double?, LocalDate>) {
        if (water.first != null) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(watering = plant.watering.plus(water as Pair<Double, LocalDate>))
                } else {
                    plant
                }
            }
            Log.d("$TAG water", _plants.value?.filter { it.id == navigatePlantID }.toString())
        }
    }

    fun nutrients(nutrients: Triple<String, Double, LocalDate>) {
        if (nutrients != null && nutrients.first.isNotBlank()) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(nutrients = plant.nutrients.plus(nutrients))
                } else {
                    plant
                }
            }
            Log.d("$TAG nutrients", _plants.value?.filter { it.id == navigatePlantID }.toString())
        }
    }

    fun reppelents(reppelents: Pair<String, LocalDate>) {
        if (reppelents != null && reppelents.first.isNotBlank()) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(repellents = plant.repellents.plus(reppelents))
                } else {
                    plant
                }
            }
            Log.d("$TAG reppelents", _plants.value?.filter { it.id == navigatePlantID }.toString())
        }
    }

    fun growthStateChange(growthState: Pair<Int, LocalDate>) {
        if (growthState.first != null) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(growthState = plant.growthState.plus(growthState))
                } else {
                    plant
                }
            }
            Log.d(
                "$TAG growthStateChange",
                _plants.value?.filter { it.id == navigatePlantID }.toString()
            )
        }
    }

    fun repot(repot: Triple<String, Int, LocalDate>) {
        if (repot.second != null && repot.first.isNotBlank()) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(repot = plant.repot.plus(repot))
                } else {
                    plant
                }
            }
            Log.d("$TAG repot", _plants.value?.filter { it.id == navigatePlantID }.toString())
        }
    }

    fun training(training: Pair<String, LocalDate>) {
        if (training != null && training.first.isNotBlank()) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(training = plant.training.plus(training))
                } else {
                    plant
                }
            }
            Log.d("$TAG training", _plants.value?.filter { it.id == navigatePlantID }.toString())
        }
    }

    fun light(light: Pair<Int, LocalDate>) {
        if (light != null) {
            _plants.value = _plants.value?.map { plant ->
                if (plant.id == navigatePlantID) {
                    plant.copy(light = plant.light.plus(light))
                } else {
                    plant
                }
            }
            Log.d("$TAG light", _plants.value?.filter { it.id == navigatePlantID }.toString())
        }
    }

    fun markAsDead() {
        _plantsHistory.value =
            _plants.value?.let { _plantsHistory.value?.plus(it.filter { plant: Plant -> plant.id == navigatePlantID }) }
                ?: listOf()
        _plants.value = _plants.value?.filter { plant -> plant.id != navigatePlantID }
        Log.d("$TAG markAsDead", _plantsHistory.value.toString())
    }
    //endregion
}

