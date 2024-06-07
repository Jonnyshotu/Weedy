package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "healthRecord_table")
data class HealthRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "healthRecordHealth")
    val health: Int,

    @ColumnInfo(name = "healthRecordDate")
    val date: LocalDate,
)