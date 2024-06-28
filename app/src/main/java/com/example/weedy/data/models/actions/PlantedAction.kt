package com.example.weedy.data.models.actions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Soil
import java.time.LocalDate

@Entity(
    tableName = "planted_Action_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PlantedAction(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val plantID: Long,

    val soilID: Long,

    @ColumnInfo(name = "Date")
    val date: LocalDate,
)