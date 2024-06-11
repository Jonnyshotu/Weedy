package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import java.time.LocalDate

@Entity(tableName = "lightRecord_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class LightRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val plantID: Long,

    @ColumnInfo(name = "lightRecordLightHours")
    val lightHours: Int,

    @ColumnInfo(name = "lightRecordDate")
    val date: LocalDate,
)