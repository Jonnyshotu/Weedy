package com.example.weedy.data.local.offlineData

import com.example.weedy.data.entities.Nutrients

object NutrientsProducts {
    fun loadNutrients(): List<Nutrients> {
        return listOf(
            Nutrients(1, "Root Juice BioBizz", "liquid"),
            Nutrients(2, "Bio Grow BioBizz", "liquid"),
            Nutrients(3, "Fish Mix BioBizz", "liquid"),
            Nutrients(4, "Bio Bloom BioBizz", "liquid"),
            Nutrients(5, "Top Max BioBizz", "liquid"),
            Nutrients(6, "Bio Heaven BioBizz", "liquid"),
            Nutrients(7, "Acti Vera BioBizz", "liquid"),
            Nutrients(8, "Microbes BioBizz", "powder"),
        )
    }
}