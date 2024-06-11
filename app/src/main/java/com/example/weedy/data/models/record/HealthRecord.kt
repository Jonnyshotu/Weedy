package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import java.time.LocalDate

@Entity(tableName = "healthRecord_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class HealthRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val plantID: Long,

    @ColumnInfo(name = "healthRecordHealth")
    val health: Int,

    @ColumnInfo(name = "healthRecordDate")
    val date: LocalDate,
)