package com.example.weedy.data.models.actions

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weedy.data.models.Soil
import java.time.LocalDate

@Entity(tableName = "germinationSoilAction_table")
data class GerminationSoilAction(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "germinationSoilDate")
    val germinationSoil: LocalDate,

    @Embedded ("germinationSoilAction_") val soil: Soil,

    @ColumnInfo(name = "germinationSoilVisibleDate")
    val germinationSoilVisible: LocalDate? = null,
)