package com.example.weedy.data.models.actions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(tableName = "germinationWaterAction_table")
data class GerminationWaterAction (

    @PrimaryKey (autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "germinationWaterDate")
    val germinationWater: LocalDate,

    @ColumnInfo(name = "germinationWaterVisibleDate")
    val germinationWaterVisible: LocalDate? = null,
)