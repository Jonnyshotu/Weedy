package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "measurementsRecord_table")
data class MeasurementsRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "measurementsRecordPH")
    val ph: Double,

    @ColumnInfo(name = "measurementsRecordEC")
    val ec: Double,

    @ColumnInfo(name = "measurementsRecordTemperature")
    val temperature: Double,

    @ColumnInfo(name = "measurementsRecordHumidity")
    val humidity: Double,

    @ColumnInfo(name = "measurementsRecordHeight")
    val height: Double,

    @ColumnInfo(name = "measurementsRecordDate")
    val date: LocalDate,
)