package com.example.weedy.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Nutrients
import com.example.weedy.data.entities.Soil
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


@Dao
interface PlantDao {

    //region Master Plant
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: MasterPlant): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(plantList: List<MasterPlant>)

    @Update
    suspend fun update(plant: MasterPlant)

    @Query("SELECT * FROM plant_table WHERE id == :searchID")
    suspend fun getPlantByID(searchID: Long): MasterPlant

    @Query("SELECT * FROM plant_table")
    fun getAll(): LiveData<List<MasterPlant>>

    @Query("SELECT COUNT(*) FROM plant_table")
    suspend fun getPlantListSize(): Int

    @Delete
    suspend fun deletePlant(plant: MasterPlant)

    //endregion

    //region Nutrients
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrient(nutrient: Nutrients)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrientList(nutrientList: List<Nutrients>)

    @Query("SELECT * FROM nutrients_table")
    fun getAllNutrients(): LiveData<List<Nutrients>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNutrient(nutrient: Nutrients)
    //endregion

    //region Soil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoil(soil: Soil)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoilList(soilList: List<Soil>)

    @Query("SELECT * FROM soil_table")
    fun getAllSoilTypes(): LiveData<List<Soil>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSoil(soil: Soil)
    //endregion

    //region GerminationWater
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGermination(germination: GerminationAction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGermination(germination: GerminationAction)

    //endregion

    //region Planted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanted(planted: PlantedAction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanted(planted: PlantedAction)

    //endregion

    //region Repot
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepot(repot: RepotAction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRepot(repot: RepotAction)

    //endregion

    //region Growth State Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrowthStateRecord(growthState: GrowthStateRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGrowthStateRecord(growthState: GrowthStateRecord)

    //endregion

    //region Health Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(health: HealthRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHealthRecord(health: HealthRecord)

    //endregion

    //region Images Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagesRecord(images: ImagesRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateImagesRecord(images: ImagesRecord)

    //endregion

    //region Light
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLightRecord(light: LightRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLightRecord(light: LightRecord)

    //endregion

    //region Measurements Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurementsRecord(measurement: MeasurementsRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMeasurementsRecord(measurement: MeasurementsRecord)

    //endregion

    //region Nutrients Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrientsRecord(nutrients: NutrientsRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNutrientsRecord(nutrients: NutrientsRecord)

    //endregion

    //region Repellents Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepellentsRecord(repellent: RepellentsRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRepellentsRecord(repellent: RepellentsRecord)

    //endregion

    //region Training Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainingRecord(training: TrainingRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTrainingRecord(training: TrainingRecord)

    //endregion

    //region Watering Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringRecord(watering: WateringRecord)

    @Query("SELECT * FROM watering_record_table WHERE plantID == :plantID ORDER BY id DESC")
    fun getWateringRecordByPlantID(plantID: Long) : LiveData<List<WateringRecord>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWateringRecord(watering: WateringRecord)

    //endregion
}