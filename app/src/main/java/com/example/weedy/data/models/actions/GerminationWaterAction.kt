package com.example.weedy.data.models.actions

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import java.time.LocalDate

@Entity(
    tableName = "germinationWaterAction_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GerminationWaterAction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val plantID: Long,

    val germinationWater: LocalDate,

    val germinationWaterVisible: LocalDate? = null,
)