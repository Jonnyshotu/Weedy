package com.example.weedy.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genetic_table")
data class Genetic(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "geneticManufacturer")
    var manufacturer: String,

    @ColumnInfo(name = "geneticSativa")
    val sativa: Int,

    @ColumnInfo(name = "geneticIndica")
    val indica: Int,

    @ColumnInfo(name = "geneticRuderalis")
    val ruderalis: Int,

    @ColumnInfo(name = "geneticBreedingType")
    val breedingType: String,

    @ColumnInfo(name = "geneticFloweringTime")
    val floweringTime: Int,
)