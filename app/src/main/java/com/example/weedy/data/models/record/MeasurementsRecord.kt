package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import java.time.LocalDate

@Entity(tableName = "measurementsRecord_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class MeasurementsRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val plantID: Long,

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