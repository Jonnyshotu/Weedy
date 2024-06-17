package com.example.weedy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_table")
data class MasterPlant(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "Local Genetic ID")
    val localGeneticID: String? = null,

    @ColumnInfo(name = "Remote Genetic ID")
    val remoteGeneticID: String? = null,

    @ColumnInfo(name = "Sativa")
    val sativa: Int?,

    @ColumnInfo(name = "Indica")
    val indica: Int?,

    @ColumnInfo(name = "Ruderalis")
    val ruderalis: Int?,

    @ColumnInfo(name = "Breeding type")
    val breedingType: String?,

    @ColumnInfo(name = "Flowering time")
    val floweringTime: Int?,

)