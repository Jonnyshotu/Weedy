package com.example.weedy.data.local.offlineData
import com.example.weedy.data.models.Soil

object SoilProducts {
    fun loadSoilTypes(): List<Soil>{
        return listOf(
            Soil(0,"Light Mix BioBizz"),
            Soil(0,"All Mix BioBizz" ),
            Soil(0,"Coco BioBizz"),
        )
    }
}