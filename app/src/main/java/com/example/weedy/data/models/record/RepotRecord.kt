package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import java.time.LocalDate

@Entity(
    tableName = "repot_action_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RepotRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val plantID: Long,

    val soilID: Long,

    @ColumnInfo(name = "Soil")
    val soilName: String,

    @ColumnInfo(name = "Pot size")
    val potSize: Double,

    @ColumnInfo(name = "Date")
    val date: LocalDate,
)