package com.example.weedy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genetic_table")
data class Genetic(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "Strain")
    val strainName: String,

    @ColumnInfo(name = "OCPC")
    val stainOCPC: String,

    @ColumnInfo(name = "Seed Company")
    var seedCompany: String?,

    @ColumnInfo(name = "Seed Company OCPC")
    var seedCompanyOCPC: String?,

    @ColumnInfo(name = "Parent OCPC")
    val parentOCPC: String?,

    @ColumnInfo(name = "Parent Names")
    val parentNames: String?,

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