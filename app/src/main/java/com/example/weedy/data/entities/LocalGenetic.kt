package com.example.weedy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_genetic_table")
data class LocalGenetic(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Strain")
    val strainName: String,

    @ColumnInfo(name = "Strain type")
    val strainType: String?,

    @ColumnInfo(name = "Image URL")
    val strainImageURL: String?,

    @ColumnInfo(name = "THC level")
    val thcLevel: String?,

    @ColumnInfo(name = "Most common terpene")
    val mostCommonTerpene: String?,

    @ColumnInfo(name = "Description")
    val description: String?,

    @ColumnInfo(name = "Effects")
    val effects: String?,
)