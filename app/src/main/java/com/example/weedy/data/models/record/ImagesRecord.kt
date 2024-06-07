package com.example.weedy.data.models.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "imagesRecord_table")
data class ImagesRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "imagesRecordImageResource")
    val imageResource: Int,

    @ColumnInfo(name = "imagesRecordDate")
    val date: LocalDate,
)