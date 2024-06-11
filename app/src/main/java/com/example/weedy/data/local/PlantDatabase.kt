package com.example.weedy.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Genetic
import com.example.weedy.data.entities.Nutrients
import com.example.weedy.data.entities.Soil
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

@Database(
    entities = [
        MasterPlant::class,
        Genetic::class,
        Nutrients::class,
        Soil::class,
        GerminationWaterAction::class,
        GerminationSoilAction::class,
        PlantedAction::class,
        RepotAction::class,
        GrowthStateRecord::class,
        HealthRecord::class,
        ImagesRecord::class,
        LightRecord::class,
        MeasurementsRecord::class,
        NutrientsRecord::class,
        RepellentsRecord::class,
        TrainingRecord::class,
        WateringRecord::class,
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class PlantDatabase : RoomDatabase() {

    abstract val plantDao: PlantDao
}

private val TAG = "PlantDatabase"

private lateinit var dbInstance: PlantDatabase

fun getDatabase(context: Context): PlantDatabase {
    synchronized(PlantDatabase::class.java) {
        if (!::dbInstance.isInitialized) {

            dbInstance = Room.databaseBuilder(
                context.applicationContext,
                PlantDatabase::class.java,
                "Plant_Database"
            ).build()
        }
    }
    Log.d(TAG, " Database initialized")
    return dbInstance
}