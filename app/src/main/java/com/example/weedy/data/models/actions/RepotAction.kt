package com.example.weedy.data.models.actions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Soil
import java.time.LocalDate

@Entity(
    tableName = "repotAction_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Soil::class,
            parentColumns = ["id"],
            childColumns = ["soilID"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class RepotAction(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val plantID: Long,

    val soilID: Long,

    @ColumnInfo(name = "repotPotSize")
    val potSize: Double,

    @ColumnInfo(name = "repotDate")
    val date: LocalDate,
)