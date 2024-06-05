package com.example.weedy.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val strain: String,
    val genetic: Genetic,
    val germinationWater: LocalDate,
    val soil: String,
    val germinationSoil: LocalDate,
    val planted: LocalDate,
    val floweringTime: Int,
    var isChecked: Boolean,
    var health: Int,
    var images: List<Pair<Int, LocalDate>> = listOf(),
    var watering: List<Pair<Double, LocalDate>> = listOf(),
    var nutrients: List<Triple<String, Double, LocalDate>> = listOf(),
    var repellents: List<Pair<String, LocalDate>> = listOf(),
    var growthState: List<Pair<Int, LocalDate>> = listOf(),
    var repot: List<Triple<String, Int, LocalDate>> = listOf(),
    var training: List<Pair<String, LocalDate>> = listOf(),
    var measurements: List<Pair<Measurements, LocalDate>> = listOf(),
    var light: List<Pair<Int, LocalDate>> = listOf(),
    var manufacturer: String = ""
) {



    //region Equals
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Plant
        return id == other.id && strain == other.strain && genetic == other.genetic &&
                germinationWater == other.germinationWater && germinationSoil == other.germinationSoil &&
                soil == other.soil && planted == other.planted
    }

    override fun hashCode(): Int {
        return id.hashCode() * 31 + strain.hashCode() * 31 + genetic.hashCode() * 31 +
                germinationWater.hashCode() * 31 + germinationSoil.hashCode() * 31 +
                soil.hashCode() * 31 + planted.hashCode()
    }
    //endregion
}



