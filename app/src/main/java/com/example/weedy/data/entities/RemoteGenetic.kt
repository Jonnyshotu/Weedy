package com.example.weedy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_genetic_table")
data class RemoteGenetic(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "OCPC")
    val stainOCPC: String,

    @ColumnInfo(name = "Strain")
    val strainName: String,

    @ColumnInfo(name = "Seed Company")
    var seedCompany: String?,

    @ColumnInfo(name = "Seed Company OCPC")
    var seedCompanyOCPC: String?,

    @ColumnInfo(name = "Parent OCPC")
    val parentOCPC: String?,

    @ColumnInfo(name = "Parent Names")
    val parentNames: String?,

    @ColumnInfo(name = "Children")
    val children: String?,

    @ColumnInfo(name = "Lineage")
    val lineage: String?,

    )
