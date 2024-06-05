package com.example.weedy.data.offlineData
import com.example.weedy.data.models.Nutrients

object NutrientsProducts {
    fun loadNutrients () : List<Nutrients>{
        return  listOf(
            Nutrients("Root Juice BioBizz", "liquid"),
            Nutrients("Bio Grow BioBizz", "liquid"),
            Nutrients("Fish Mix BioBizz", "liquid"),
            Nutrients("Bio Bloom BioBizz", "liquid"),
            Nutrients("Top Max BioBizz", "liquid"),
            Nutrients("Bio Heaven BioBizz", "liquid"),
            Nutrients("Acti Vera BioBizz", "liquid"),
            Nutrients("Microbes BioBizz", "solid"),
        )
    }
}