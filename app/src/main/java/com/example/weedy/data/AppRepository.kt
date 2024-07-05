package com.example.weedy.data

import androidx.lifecycle.LiveData
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.entities.Nutrients
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.models.actions.GerminationAction
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

class AppRepository(private val database: PlantDatabase) {

    private val TAG = "Repository"

    val plantMasterList = database.plantDao.getAll()

    val nutrientList = database.plantDao.getAllNutrients()

    val soilList = database.plantDao.getAllSoilTypes()


    suspend fun insertPlanted(planted: PlantedAction) = database.plantDao.insertPlanted(planted)
    suspend fun insertGermination(germination: GerminationAction) =
        database.plantDao.insertGermination(germination)

    suspend fun insertRepot(repot: RepotAction) = database.plantDao.insertRepot(repot)
    suspend fun insertGrowthState(state: GrowthStateRecord) =
        database.plantDao.insertGrowthStateRecord(state)

    suspend fun insertHealth(health: HealthRecord) = database.plantDao.insertHealthRecord(health)
    suspend fun insertImage(images: ImagesRecord) = database.plantDao.insertImagesRecord(images)
    suspend fun insertLight(light: LightRecord) = database.plantDao.insertLightRecord(light)
    suspend fun insertMeasurement(measurement: MeasurementsRecord) =
        database.plantDao.insertMeasurementsRecord(measurement)

    suspend fun insertNutrient(nutrients: NutrientsRecord) =
        database.plantDao.insertNutrientsRecord(nutrients)

    suspend fun insertRepellent(repellent: RepellentsRecord) =
        database.plantDao.insertRepellentsRecord(repellent)

    suspend fun insertTraining(training: TrainingRecord) =
        database.plantDao.insertTrainingRecord(training)

    suspend fun insertWatering(watering: WateringRecord) =
        database.plantDao.insertWateringRecord(watering)

    fun getWateringRecordByPlantID(plantID: Long): LiveData<List<WateringRecord>> {
        return database.plantDao.getWateringRecordByPlantID(plantID)
    }

    suspend fun getPlantByID(searchID: Long): MasterPlant = database.plantDao.getPlantByID(searchID)
    suspend fun insertPlant(plant: MasterPlant): Long = database.plantDao.insert(plant)
    suspend fun updatePlant(plant: MasterPlant) = database.plantDao.update(plant)
    suspend fun deletePlant(plant: MasterPlant) = database.plantDao.deletePlant(plant)
    suspend fun loadNutrientList(nutrientList: List<Nutrients>) =
        database.plantDao.insertNutrientList(nutrientList)

    suspend fun loadSoilList(soilList: List<Soil>) = database.plantDao.insertSoilList(soilList)
}
