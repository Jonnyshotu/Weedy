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
import com.example.weedy.data.models.DisplayPlant
import com.example.weedy.data.models.actions.GerminationAction
import com.example.weedy.data.models.actions.PlantedAction
import com.example.weedy.data.models.record.RepotRecord
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
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * ViewModel for managing UI-related data in a lifecycle-conscious way.
 *
 * @property application The application context used for accessing resources and database.
 */
class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "ViewModel"

    // Database instance
    private val database = getDatabase(application)

    // Repository instances for app and strain data
    private val appRepository = AppRepository(database)
    private val strainRepository = StrainRepository(database, application.applicationContext)

    // LiveData to hold the current plant being observed
    private var _plant = MutableLiveData<MasterPlant>()
    val plant: LiveData<MasterPlant> get() = _plant

    // LiveData to hold the list of display plants
    private var _displayPlantList = MutableLiveData<List<DisplayPlant>>()
    val displayPlantList: LiveData<List<DisplayPlant>> get() = _displayPlantList

    // LiveData sources from repositories
    val plantList = appRepository.plantMasterList
    val healthRecordsList = appRepository.healthRecordsList
    val growthRecordsList = appRepository.growthStateRecordList
    val localGeneticCollection = strainRepository.localGeneticCollection
    val remoteGeneticCollection = strainRepository.remoteGeneticCollection
    val nutrientsList = appRepository.nutrientList
    val soilList = appRepository.soilList

    init {
        loadLocalGenetics()
        loadSoilTypes()
        loadNutrients()
    }

    //region Home Fragment

    /**
     * Creates a list of display plants from the given list of master plants.
     *
     * @param masterPlants List of [MasterPlant] objects to be converted into [DisplayPlant] objects.
     */
    fun createDisplayPlants(masterPlants: List<MasterPlant>) {
        _displayPlantList.value = masterPlants.map { masterPlant ->
            DisplayPlant(
                strainName = masterPlant.strainName,
                masterPlantID = masterPlant.id,
                flowerTime = masterPlant.floweringTime,
                growthState = null,
                health = null,
                lastCheckup = null,
                plantImage = null,
                age = null
            )
        }
        Log.d(TAG, "display plants created: ${displayPlantList.value?.size}")
        updateHealthStatus()
        updateAge()
    }

    /**
     * Updates the health status of display plants based on the latest health records.
     */
    fun updateHealthStatus() {
        val updatedDisplayPlants = displayPlantList.value?.map { displayPlant ->
            val latestHealthRecord =
                healthRecordsList.value?.filter { healthRecords -> healthRecords.plantID == displayPlant.masterPlantID }
                    ?.maxByOrNull { healthRecord -> healthRecord.id }

            displayPlant.copy(health = latestHealthRecord?.health)
        } ?: emptyList()
        Log.d(TAG, "update health status display plants size: ${updatedDisplayPlants.size}")
        _displayPlantList.value = updatedDisplayPlants
    }

    /**
     * Updates the age of display plants based on the growth state records.
     */
    fun updateAge() {
        val updatedDisplayPlants = displayPlantList.value?.map { displayPlant ->
            val growthStateRecordList =
                growthRecordsList.value?.filter { growthRecords -> growthRecords.plantID == displayPlant.masterPlantID }

            val latestEntry = growthStateRecordList?.maxByOrNull { it.id }

            val startFlowering = growthStateRecordList?.find { it.growthState == 5 }

            var ageInFlowering: Long? = null

            if (startFlowering != null) {
                ageInFlowering = ChronoUnit.DAYS.between(startFlowering.date, LocalDate.now())
            }

            displayPlant.copy(age = ageInFlowering?.toInt(), growthState = latestEntry?.growthState)
        } ?: emptyList()
        Log.d(TAG, "update age display plants size: ${updatedDisplayPlants.size}")
        _displayPlantList.value = updatedDisplayPlants
    }

    //endregion

    //region Load

    /**
     * Loads nutrient data into the repository.
     */
    private fun loadNutrients() {
        viewModelScope.launch {
            appRepository.loadNutrientList(NutrientsProducts.loadNutrients())
        }
    }

    /**
     * Loads soil type data into the repository.
     */
    private fun loadSoilTypes() {
        viewModelScope.launch {
            appRepository.loadSoilList(SoilProducts.loadSoilTypes())
        }
    }

    /**
     * Loads local genetic strains into the repository.
     */
    fun loadLocalGenetics() {
        viewModelScope.launch {
            strainRepository.getLocalStrains()
        }
    }

    /**
     * Loads remote genetic strains into the repository.
     */
    fun loadRemoteGenetics() {
        viewModelScope.launch {
            strainRepository.getRemoteStrains()
        }
    }

    //endregion

    //region Update

    /**
     * Updates an existing plant in the repository.
     *
     * @param plant The [MasterPlant] object to be updated.
     */
    fun updatePlant(plant: MasterPlant) {
        viewModelScope.launch {
            appRepository.updatePlant(plant)
        }
    }

    //endregion

    //region Delete

    /**
     * Deletes a plant from the repository.
     *
     * @param plant The [MasterPlant] object to be deleted.
     */
    fun deletePlant(plant: MasterPlant) {
        viewModelScope.launch {
            appRepository.deletePlant(plant)
        }
    }

    /**
     * Deletes a plant by its ID from the repository.
     *
     * @param masterPlantID The ID of the plant to be deleted.
     */
    fun deletePlantByID(masterPlantID: Long) {
        viewModelScope.launch {
            val plant = appRepository.getPlantByID(masterPlantID)
            deletePlant(plant)
        }
    }

    //endregion

    //region Insert

    /**
     * Inserts a new plant into the repository.
     *
     * @param plant The [MasterPlant] object to be inserted.
     * @param onResult Callback function to receive the ID of the inserted plant.
     */
    fun insertPlant(plant: MasterPlant, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = appRepository.insertPlant(plant)
            onResult(id)
            Log.d(TAG, "Inserted plant ID: $id")
        }
    }

    /**
     * Inserts a repot record into the repository.
     *
     * @param repot The [RepotRecord] object to be inserted.
     */
    fun insertRepot(repot: RepotRecord) {
        viewModelScope.launch {
            appRepository.insertRepot(repot)
            Log.d(TAG, "Insert repot record: $repot")
        }
    }

    /**
     * Inserts a germination action record into the repository.
     *
     * @param germination The [GerminationAction] object to be inserted.
     */
    fun insertGermination(germination: GerminationAction) {
        viewModelScope.launch {
            appRepository.insertGermination(germination)
            Log.d(TAG, "Insert germination record: $germination")
        }
    }

    /**
     * Inserts a planted action record into the repository.
     *
     * @param planted The [PlantedAction] object to be inserted.
     */
    fun insertPlanted(planted: PlantedAction) {
        viewModelScope.launch {
            appRepository.insertPlanted(planted)
            Log.d(TAG, "Insert planted record: $planted")
        }
    }

    /**
     * Inserts a growth state record into the repository.
     *
     * @param state The [GrowthStateRecord] object to be inserted.
     */
    fun insertGrowthState(state: GrowthStateRecord) {
        viewModelScope.launch {
            appRepository.insertGrowthState(state)
            Log.d(TAG, "Insert state record: $state")
        }
    }

    /**
     * Inserts a health record into the repository.
     *
     * @param health The [HealthRecord] object to be inserted.
     */
    fun insertHealth(health: HealthRecord) {
        viewModelScope.launch {
            appRepository.insertHealth(health)
            Log.d(TAG, "Insert health record: $health")
        }
    }

    /**
     * Inserts an image record into the repository.
     *
     * @param images The [ImagesRecord] object to be inserted.
     */
    fun insertImage(images: ImagesRecord) {
        viewModelScope.launch {
            appRepository.insertImage(images)
            Log.d(TAG, "Insert images record: $images")
        }
    }

    /**
     * Inserts a light record into the repository.
     *
     * @param light The [LightRecord] object to be inserted.
     */
    fun insertLight(light: LightRecord) {
        viewModelScope.launch {
            appRepository.insertLight(light)
            Log.d(TAG, "Insert light record: $light")
        }
    }

    /**
     * Inserts a measurement record into the repository.
     *
     * @param measurement The [MeasurementsRecord] object to be inserted.
     */
    fun insertMeasurement(measurement: MeasurementsRecord) {
        viewModelScope.launch {
            appRepository.insertMeasurement(measurement)
            Log.d(TAG, "Insert measurement record: $measurement")
        }
    }

    /**
     * Inserts a nutrient record into the repository.
     *
     * @param nutrients The [NutrientsRecord] object to be inserted.
     */
    fun insertNutrient(nutrients: NutrientsRecord) {
        viewModelScope.launch {
            appRepository.insertNutrient(nutrients)
            Log.d(TAG, "Insert nutrients record: $nutrients")
        }
    }

    /**
     * Inserts a repellent record into the repository.
     *
     * @param repellent The [RepellentsRecord] object to be inserted.
     */
    fun insertRepellent(repellent: RepellentsRecord) {
        viewModelScope.launch {
            appRepository.insertRepellent(repellent)
            Log.d(TAG, "Insert repellent record: $repellent")
        }
    }

    /**
     * Inserts a training record into the repository.
     *
     * @param training The [TrainingRecord] object to be inserted.
     */
    fun insertTraining(training: TrainingRecord) {
        viewModelScope.launch {
            try {
                appRepository.insertTraining(training)
                Log.d(TAG, "Insert training record: $training")
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting training record", e)
            }
        }
    }

    /**
     * Inserts a watering record into the repository.
     *
     * @param watering The [WateringRecord] object to be inserted.
     */
    fun insertWatering(watering: WateringRecord) {
        viewModelScope.launch {
            appRepository.insertWatering(watering)
            Log.d(TAG, "Insert watering record: $watering")
        }
    }

    //endregion

    //region Get by ID

    /**
     * Retrieves a plant by its ID and updates the LiveData with the result.
     *
     * @param searchID The ID of the plant to retrieve.
     */
    fun getPlantByID(searchID: Long) {
        viewModelScope.launch {
            val plant = appRepository.getPlantByID(searchID)
            _plant.value = plant
        }
    }

    /**
     * Retrieves watering records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve watering records.
     * @return LiveData containing the list of [WateringRecord] objects.
     */
    fun getWateringRecordByPlantID(plantID: Long): LiveData<List<WateringRecord>> =
        appRepository.getWateringRecordByPlantID(plantID)

    /**
     * Retrieves nutrient records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve nutrient records.
     * @return LiveData containing the list of [NutrientsRecord] objects.
     */
    fun getNutrientsRecordByPlantID(plantID: Long): LiveData<List<NutrientsRecord>> =
        appRepository.getNutrientsRecordByPlantID(plantID)

    /**
     * Retrieves repot actions for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve repot actions.
     * @return LiveData containing the list of [RepotRecord] objects.
     */
    fun getRepotActionsByPlantID(plantID: Long): LiveData<List<RepotRecord>> =
        appRepository.getRepotActionsByPlantID(plantID)

    /**
     * Retrieves growth state records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve growth state records.
     * @return LiveData containing the list of [GrowthStateRecord] objects.
     */
    fun getGrowthStateRecordsByPlantID(plantID: Long): LiveData<List<GrowthStateRecord>> =
        appRepository.getGrowthStateRecordsByPlantID(plantID)

    /**
     * Retrieves health records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve health records.
     * @return LiveData containing the list of [HealthRecord] objects.
     */
    fun getHealthRecordByPlantID(plantID: Long): LiveData<List<HealthRecord>> =
        appRepository.getHealthRecordByPlantID(plantID)

    /**
     * Retrieves image records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve image records.
     * @return LiveData containing the list of [ImagesRecord] objects.
     */
    fun getImagesRecordByPlantID(plantID: Long): LiveData<List<ImagesRecord>> =
        appRepository.getImagesRecordByPlantID(plantID)

    /**
     * Retrieves light records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve light records.
     * @return LiveData containing the list of [LightRecord] objects.
     */
    fun getLightRecordsByPlantID(plantID: Long): LiveData<List<LightRecord>> =
        appRepository.getLightRecordsByPlantID(plantID)

    /**
     * Retrieves measurement records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve measurement records.
     * @return LiveData containing the list of [MeasurementsRecord] objects.
     */
    fun getMeasurementsRecordByPlantID(plantID: Long): LiveData<List<MeasurementsRecord>> =
        appRepository.getMeasurementsRecordByPlantID(plantID)

    /**
     * Retrieves repellent records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve repellent records.
     * @return LiveData containing the list of [RepellentsRecord] objects.
     */
    fun getRepellentsRecordByPlantID(plantID: Long): LiveData<List<RepellentsRecord>> =
        appRepository.getRepellentsRecordByPlantID(plantID)

    /**
     * Retrieves training records for a plant by its ID.
     *
     * @param plantID The ID of the plant for which to retrieve training records.
     * @return LiveData containing the list of [TrainingRecord] objects.
     */
    fun getTrainingRecordByPlantID(plantID: Long): LiveData<List<TrainingRecord>> =
        appRepository.getTrainingRecordByPlantID(plantID)

    //endregion
}
