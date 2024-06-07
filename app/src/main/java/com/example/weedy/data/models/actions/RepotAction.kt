package com.example.weedy.data.models.actions

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weedy.data.models.Soil
import java.time.LocalDate

@Entity(tableName = "repotAction_table")
data class RepotAction(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @Embedded("repotAction") val soil: Soil,

    @ColumnInfo(name = "repotPotSize")
    val potSize: Double,

    @ColumnInfo(name = "repotDate")
    val date: LocalDate,
)