package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import java.time.LocalDate

@Entity(tableName = "measurements_record_table",
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

    @ColumnInfo(name = "PH")
    val ph: Double,

    @ColumnInfo(name = "EC")
    val ec: Double,

    @ColumnInfo(name = "Temperature")
    val temperature: Double,

    @ColumnInfo(name = "Humidity")
    val humidity: Double,

    @ColumnInfo(name = "Height")
    val height: Double,

    @ColumnInfo(name = "Date")
    val date: LocalDate,
)