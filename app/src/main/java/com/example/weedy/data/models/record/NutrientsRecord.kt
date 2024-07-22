package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Nutrients
import java.time.LocalDate

@Entity(
    tableName = "nutrients_record_table",
    foreignKeys = [
        ForeignKey(
            entity = MasterPlant::class,
            parentColumns = ["id"],
            childColumns = ["plantID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NutrientsRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val plantID: Long,

    val nutrientID: Long,

    @ColumnInfo(name = "Nutrient")
    val nutrientName: String,

    @ColumnInfo(name = "Amount")
    val amount: Double,

    @ColumnInfo(name = "Date")
    val date: LocalDate,
)