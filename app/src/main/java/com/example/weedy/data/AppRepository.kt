package com.example.weedy.data

import androidx.lifecycle.LiveData
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.entities.Nutrients
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.entities.MasterPlant
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

/**
 * Repository class for managing plant-related data operations.
 *
 * @param database The PlantDatabase instance for database operations.
 */
class AppRepository(private val database: PlantDatabase) {

    private val TAG = "Repository"

    //region Live Data

    //Retrieves all master plant records from the database.
    val plantMasterList = database.plantDao.getAll()

    // Retrieves all nutrient records from the database.
    val nutrientList = database.plantDao.getAllNutrients()

    // Retrieves all soil type records from the database.
    val soilList = database.plantDao.getAllSoilTypes()

    // Retrieves all health records from the database.
    val healthRecordsList = database.plantDao.getAllHealthRecords()

    // Retrieves all image records from the database.
    val imageRecordsList = database.plantDao.getAllImagesRecords()

    // Retrieves all growth state records from the database.
    val growthStateRecordList = database.plantDao.getAllGrowthStateRecords()

    //endregion

    //region Insert

    /**
     * Inserts a new plant record into the database.
     *
     * @param plant The MasterPlant object to be inserted.
     * @return The ID of the inserted plant.
     */
    suspend fun insertPlant(plant: MasterPlant): Long = database.plantDao.insert(plant)

    /**
     * Inserts a new planted action record into the database.
     *
     * @param planted The PlantedAction object to be inserted.
     */
    suspend fun insertPlanted(planted: PlantedAction) = database.plantDao.insertPlanted(planted)

    /**
     * Inserts a new germination action record into the database.
     *
     * @param germination The GerminationAction object to be inserted.
     */
    suspend fun insertGermination(germination: GerminationAction) =
        database.plantDao.insertGermination(germination)

    /**
     * Inserts a new repot record into the database.
     *
     * @param repot The RepotRecord object to be inserted.
     */
    suspend fun insertRepot(repot: RepotRecord) = database.plantDao.insertRepot(repot)

    /**
     * Inserts a new growth state record into the database.
     *
     * @param state The GrowthStateRecord object to be inserted.
     */
    suspend fun insertGrowthState(state: GrowthStateRecord) =
        database.plantDao.insertGrowthStateRecord(state)

    /**
     * Inserts a new health record into the database.
     *
     * @param health The HealthRecord object to be inserted.
     */
    suspend fun insertHealth(health: HealthRecord) = database.plantDao.insertHealthRecord(health)

    /**
     * Inserts a new images record into the database.
     *
     * @param images The ImagesRecord object to be inserted.
     */
    suspend fun insertImage(images: ImagesRecord) = database.plantDao.insertImagesRecord(images)

    /**
     * Inserts a new light record into the database.
     *
     * @param light The LightRecord object to be inserted.
     */
    suspend fun insertLight(light: LightRecord) = database.plantDao.insertLightRecord(light)

    /**
     * Inserts a new measurement record into the database.
     *
     * @param measurement The MeasurementsRecord object to be inserted.
     */
    suspend fun insertMeasurement(measurement: MeasurementsRecord) =
        database.plantDao.insertMeasurementsRecord(measurement)

    /**
     * Inserts a new nutrients record into the database.
     *
     * @param nutrients The NutrientsRecord object to be inserted.
     */
    suspend fun insertNutrient(nutrients: NutrientsRecord) =
        database.plantDao.insertNutrientsRecord(nutrients)

    /**
     * Inserts a new repellent record into the database.
     *
     * @param repellent The RepellentsRecord object to be inserted.
     */
    suspend fun insertRepellent(repellent: RepellentsRecord) =
        database.plantDao.insertRepellentsRecord(repellent)

    /**
     * Inserts a new training record into the database.
     *
     * @param training The TrainingRecord object to be inserted.
     */
    suspend fun insertTraining(training: TrainingRecord) =
        database.plantDao.insertTrainingRecord(training)

    /**
     * Inserts a new watering record into the database.
     *
     * @param watering The WateringRecord object to be inserted.
     */
    suspend fun insertWatering(watering: WateringRecord) =
        database.plantDao.insertWateringRecord(watering)

    //endregion

    //region Get by ID

    /**
     * Retrieves watering records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which watering records are retrieved.
     * @return A LiveData object containing a list of WateringRecord.
     */
    fun getWateringRecordByPlantID(plantID: Long): LiveData<List<WateringRecord>> =
        database.plantDao.getWateringRecordByPlantID(plantID)

    /**
     * Retrieves nutrients records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which nutrients records are retrieved.
     * @return A LiveData object containing a list of NutrientsRecord.
     */
    fun getNutrientsRecordByPlantID(plantID: Long): LiveData<List<NutrientsRecord>> =
        database.plantDao.getNutrientsRecordByPlantID(plantID)

    /**
     * Retrieves repot actions for a specific plant ID.
     *
     * @param plantID The ID of the plant for which repot actions are retrieved.
     * @return A LiveData object containing a list of RepotRecord.
     */
    fun getRepotActionsByPlantID(plantID: Long): LiveData<List<RepotRecord>> =
        database.plantDao.getRepotActionsByPlantID(plantID)

    /**
     * Retrieves growth state records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which growth state records are retrieved.
     * @return A LiveData object containing a list of GrowthStateRecord.
     */
    fun getGrowthStateRecordsByPlantID(plantID: Long): LiveData<List<GrowthStateRecord>> =
        database.plantDao.getGrowthStateRecordsByPlantID(plantID)

    /**
     * Retrieves health records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which health records are retrieved.
     * @return A LiveData object containing a list of HealthRecord.
     */
    fun getHealthRecordByPlantID(plantID: Long): LiveData<List<HealthRecord>> =
        database.plantDao.getHealthRecordsByPlantID(plantID)

    /**
     * Retrieves images records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which images records are retrieved.
     * @return A LiveData object containing a list of ImagesRecord.
     */
    fun getImagesRecordByPlantID(plantID: Long): LiveData<List<ImagesRecord>> =
        database.plantDao.getImagesRecordByPlantID(plantID)

    /**
     * Retrieves light records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which light records are retrieved.
     * @return A LiveData object containing a list of LightRecord.
     */
    fun getLightRecordsByPlantID(plantID: Long): LiveData<List<LightRecord>> =
        database.plantDao.getLightRecordsByPlantID(plantID)

    /**
     * Retrieves measurements records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which measurements records are retrieved.
     * @return A LiveData object containing a list of MeasurementsRecord.
     */
    fun getMeasurementsRecordByPlantID(plantID: Long): LiveData<List<MeasurementsRecord>> =
        database.plantDao.getMeasurementsRecordByPlantID(plantID)

    /**
     * Retrieves repellents records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which repellents records are retrieved.
     * @return A LiveData object containing a list of RepellentsRecord.
     */
    fun getRepellentsRecordByPlantID(plantID: Long): LiveData<List<RepellentsRecord>> =
        database.plantDao.getRepellentsRecordByPlantID(plantID)

    /**
     * Retrieves training records for a specific plant ID.
     *
     * @param plantID The ID of the plant for which training records are retrieved.
     * @return A LiveData object containing a list of TrainingRecord.
     */
    fun getTrainingRecordByPlantID(plantID: Long): LiveData<List<TrainingRecord>> =
        database.plantDao.getTrainingRecordByPlantID(plantID)

    /**
     * Retrieves a plant by its ID.
     *
     * @param searchID The ID of the plant to retrieve.
     * @return The MasterPlant object corresponding to the given ID.
     */
    suspend fun getPlantByID(searchID: Long): MasterPlant = database.plantDao.getPlantByID(searchID)

    //endregion

    //region Update

    /**
     * Updates an existing plant record in the database.
     *-
     * @param plant The MasterPlant object with updated values.
     */
    suspend fun updatePlant(plant: MasterPlant) = database.plantDao.update(plant)

    //endregion

    //region Delete

    /**
     * Deletes a plant record from the database.
     *
     * @param plant The MasterPlant object to be deleted.
     */
    suspend fun deletePlant(plant: MasterPlant) = database.plantDao.deletePlant(plant)

    //endregion

    //region Load

    /**
     * Loads a list of nutrients into the database.
     *
     * @param nutrientList The list of Nutrients objects to be inserted.
     */
    suspend fun loadNutrientList(nutrientList: List<Nutrients>) =
        database.plantDao.insertNutrientList(nutrientList)

    /**
     * Loads a list of soil types into the database.
     *
     * @param soilList The list of Soil objects to be inserted.
     */
    suspend fun loadSoilList(soilList: List<Soil>) = database.plantDao.insertSoilList(soilList)

    //endregion
}
