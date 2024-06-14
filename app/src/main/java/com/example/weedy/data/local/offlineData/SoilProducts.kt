package com.example.weedy.data.local.offlineData
import com.example.weedy.data.entities.Soil

object SoilProducts {
    fun loadSoilTypes(): List<Soil>{
        return listOf(
            Soil(1,"Coco BioBizz"),
            Soil(2,"Light Mix BioBizz"),
            Soil(3,"All Mix BioBizz" ),
        )
    }
}