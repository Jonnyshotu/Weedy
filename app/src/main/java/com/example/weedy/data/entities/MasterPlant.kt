package com.example.weedy.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weedy.data.SeedCompany

@Entity(tableName = "plant_table")
data class MasterPlant(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val geneticID: String? = null,
)