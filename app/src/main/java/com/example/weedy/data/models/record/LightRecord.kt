package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "lightRecord_table")
data class LightRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "lightRecordLightHours")
    val lightHours: Int,

    @ColumnInfo(name = "lightRecordDate")
    val date: LocalDate,
)