package com.example.weedy.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weedy.data.models.Genetic
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
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity(tableName = "plant_table")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var isChecked: Boolean,
    val strain: String,
    @Embedded(prefix = "genetic_") val genetic: Genetic,
    @Embedded(prefix = "germinationWater_") val germinationWater: GerminationWaterAction? = null,
    @Embedded (prefix = "germinationSoil_")val germinationSoil: GerminationSoilAction? = null,
    @Embedded (prefix = "planted_")val planted: PlantedAction? = null,
    @Embedded(prefix = "repot_") var repot: RepotAction? = null,
    @Embedded (prefix = "light_")var light: LightRecord? = null,
    @Embedded (prefix = "health_")var health: HealthRecord? = null,
    @Embedded(prefix = "images_") var images: ImagesRecord? = null,
    @Embedded (prefix = "watering_")var watering: WateringRecord? = null,
    @Embedded (prefix = "nutrients_")var nutrients: NutrientsRecord? = null,
    @Embedded (prefix = "repellents_")var repellents: RepellentsRecord? = null,
    @Embedded(prefix = "training_") var training: TrainingRecord? = null,
    @Embedded(prefix = "measurements_") var measurements: MeasurementsRecord? = null,
    @Embedded (prefix = "growthState_")var growthState: GrowthStateRecord? = null,
) {
    val harvestDate: LocalDate?
        get() = planted?.date?.plusWeeks(genetic.floweringTime.toLong())
    val weeksOld: Long
        get() = ChronoUnit.WEEKS.between(planted?.date, LocalDate.now())

    //region Equals
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Plant
        return id == other.id &&
                isChecked == other.isChecked &&
                strain == other.strain &&
                genetic == other.genetic &&
                germinationWater == other.germinationWater &&
                germinationSoil == other.germinationSoil &&
                planted == other.planted &&
                repot == other.repot &&
                light == other.light &&
                health == other.health &&
                images == other.images &&
                watering == other.watering &&
                nutrients == other.nutrients &&
                repellents == other.repellents &&
                training == other.training &&
                measurements == other.measurements &&
                growthState == other.growthState
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + isChecked.hashCode()
        result = 31 * result + strain.hashCode()
        result = 31 * result + genetic.hashCode()
        result = 31 * result + germinationWater.hashCode()
        result = 31 * result + germinationSoil.hashCode()
        result = 31 * result + planted.hashCode()
        result = 31 * result + repot.hashCode()
        result = 31 * result + light.hashCode()
        result = 31 * result + health.hashCode()
        result = 31 * result + images.hashCode()
        result = 31 * result + watering.hashCode()
        result = 31 * result + nutrients.hashCode()
        result = 31 * result + repellents.hashCode()
        result = 31 * result + training.hashCode()
        result = 31 * result + measurements.hashCode()
        result = 31 * result + growthState.hashCode()
        return result
    }
    //endregion
}
