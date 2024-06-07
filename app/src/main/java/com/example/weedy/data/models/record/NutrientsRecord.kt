package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weedy.data.models.Nutrients
import java.time.LocalDate

@Entity(tableName = "nutrientsRecord_table")
data class NutrientsRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @Embedded ("nutrientsRecord_")val nutrient: Nutrients,

    @ColumnInfo(name = "nutrientsRecordAmount")
    val amount: Double,

    @ColumnInfo(name = "nutrientsRecordDate")
    val date: LocalDate,
)