package com.example.weedy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weedy.data.entities.Plant
import com.example.weedy.data.models.Genetic
import com.example.weedy.data.models.Nutrients
import com.example.weedy.data.models.Soil
import com.example.weedy.data.models.actions.GerminationSoilAction
import com.example.weedy.data.models.actions.GerminationWaterAction
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


    //regionGermination Soil

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGerminationSoil(germination: GerminationSoilAction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGerminationSoil(germination: GerminationSoilAction)

    //endregion

    //region GerminationWater
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGerminationWater(germination: GerminationWaterAction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGerminationWater(germination: GerminationWaterAction)

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

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWateringRecord(watering: WateringRecord)

    //endregion

    //region Plant
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: Plant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(plantList: List<Plant>)

    @Update
    suspend fun update(plant: Plant)

    @Query("SELECT * FROM plant_table")
    fun getAll(): LiveData<List<Plant>>

    @Query("SELECT COUNT(*) FROM plant_table")
    suspend fun getPlantListSize(): Int

    @Delete
    suspend fun deletePlant(plant: Plant)

    //endregion

    //region Genetic
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenetic(genetic: Genetic)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGenetic(genetic: Genetic)
    //endregion

    //region Nutrients
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrient(nutrient: Nutrients)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrientList(nutrientList: List<Nutrients>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNutrient(nutrient: Nutrients)
    //endregion

    //region Soil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoil(soil: Soil)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoilList(soilList: List<Soil>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSoil(soil: Soil)
    //endregion
}