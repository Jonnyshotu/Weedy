package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(tableName = "growthStateRecord_table")
data class GrowthStateRecord (

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "growthStateRecordGrowthState")
    val growthState: Int,

    @ColumnInfo(name = "growthStateRecordDate")
    val date: LocalDate,
)