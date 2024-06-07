package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(tableName = "trainingRecord_table")
data class TrainingRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "trainingRecordTrainingType")
    val trainingType: String,

    @ColumnInfo(name = "trainingRecordDate")
    val date: LocalDate,
)