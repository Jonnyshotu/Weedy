package com.example.weedy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "nutrients_table")
data class Nutrients(

    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo(name = "nutrientsName")
    val name: String,

    @ColumnInfo(name = "nutrientsType")
    val type: String,
)