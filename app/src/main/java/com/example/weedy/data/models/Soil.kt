package com.example.weedy.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soil_table")
data class Soil(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "soilName")
    val name: String,
)