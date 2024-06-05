package com.example.weedy.data.offlineData
import com.example.weedy.data.models.Soil

object SoilProducts {
    fun loadSoilTypes(): List<Soil>{
        return listOf(
            Soil("Light Mix BioBizz"),
            Soil("All Mix BioBizz" ),
            Soil("Coco BioBizz"),
        )
    }
}