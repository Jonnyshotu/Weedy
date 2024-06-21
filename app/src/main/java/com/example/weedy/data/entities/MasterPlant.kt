package com.example.weedy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_table")
data class MasterPlant(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "Strain")
    var strainName: String,

    @ColumnInfo(name = "Seed Company")
    var seedCompany: String,

    @ColumnInfo(name = "Local Genetic ID")
    var localGeneticID: Long? = null,

    @ColumnInfo(name = "Remote Genetic ID")
    var remoteGeneticID: String? = null,

    @ColumnInfo(name = "THC")
    var thc: String? = null,

    @ColumnInfo(name = "CBD")
    var cbd: String? = null,

    @ColumnInfo(name = "Sativa")
    var sativa: String? = null,

    @ColumnInfo(name = "Indica")
    var indica: String? = null,

    @ColumnInfo(name = "Ruderalis")
    var ruderalis: String? = null,

    @ColumnInfo(name = "Breeding type")
    var breedingType: String? = null,

    @ColumnInfo(name = "Flowering time")
    var floweringTime: Int? = null,

    @ColumnInfo(name = "Cycle time")
    var cycleTime: Int? = null,


    )