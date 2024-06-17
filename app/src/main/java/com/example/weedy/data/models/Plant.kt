package com.example.weedy.data.models

import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.RemoteGenetic
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

//Datenklasse nur zur Darstellung
data class Plant(

    var isChecked: Boolean = false,

    val masterPlant: MasterPlant,

    val localGenetic: LocalGenetic? = null,
    val remoteGenetic: RemoteGenetic? = null,

    val germinationWaterActions: List<GerminationWaterAction>? = null,
    val germinationSoilActions: List<GerminationSoilAction>? = null,
    val plantedAction: List<PlantedAction>? = null,
    val repotAction: List<RepotAction>? = null,
    val growthStateRecord: List<GrowthStateRecord>? = null,
    val healthRecord: List<HealthRecord>? = null,
    val imagesRecord: List<ImagesRecord>? = null,
    val lightRecord: List<LightRecord>? = null,
    val measurementsRecord: List<MeasurementsRecord>? = null,
    val nutrientsRecord: List<NutrientsRecord>? = null,
    val repellentsRecord: List<RepellentsRecord>? = null,
    val trainingRecord: List<TrainingRecord>? = null,
    val wateringRecord: List<WateringRecord>? = null,
)