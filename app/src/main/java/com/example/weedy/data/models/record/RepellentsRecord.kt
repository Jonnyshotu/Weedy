package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "repellentsRecord_table")
data class RepellentsRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "repellentsRecordInfestationType")
    val infestationType: String,

    @ColumnInfo(name = "repellentsRecordDate")
    val date: LocalDate,
)