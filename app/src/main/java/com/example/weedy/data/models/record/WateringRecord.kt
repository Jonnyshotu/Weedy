package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "wateringRecord_table")
data class WateringRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "wateringRecordAmount")
    val amount: Double,

    @ColumnInfo(name = "wateringRecordPH")
    val ph: Double? = null,

    @ColumnInfo(name = "wateringRecordDate")
    val date: LocalDate,
)