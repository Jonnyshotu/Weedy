package com.example.weedy.data.models.actions

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weedy.data.models.Soil
import java.time.LocalDate

@Entity(tableName = "plantedAction_table")
data class PlantedAction(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @Embedded ("plantedAction_")val soil: Soil,

    @ColumnInfo(name = "plantedDate")
    val date: LocalDate,
)