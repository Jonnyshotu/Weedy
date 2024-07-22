package com.example.weedy.data.models

import java.time.LocalDate

data class DisplayPlant(
    val strainName: String,
    val masterPlantID: Long,
    var age: Int?,
    var flowerTime: Int?,
    var growthState: Int?,
    var health: Int?,
    var lastCheckup: LocalDate?,
    var plantImage: Int?,
)