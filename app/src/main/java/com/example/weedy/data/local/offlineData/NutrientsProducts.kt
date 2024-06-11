package com.example.weedy.data.local.offlineData
import com.example.weedy.data.entities.Nutrients

object NutrientsProducts {
    fun loadNutrients () : List<Nutrients>{
        return  listOf(
            Nutrients(0,"Root Juice BioBizz", "liquid"),
            Nutrients(0,"Bio Grow BioBizz", "liquid"),
            Nutrients(0,"Fish Mix BioBizz", "liquid"),
            Nutrients(0,"Bio Bloom BioBizz", "liquid"),
            Nutrients(0,"Top Max BioBizz", "liquid"),
            Nutrients(0,"Bio Heaven BioBizz", "liquid"),
            Nutrients(0,"Acti Vera BioBizz", "liquid"),
            Nutrients(0,"Microbes BioBizz", "solid"),
        )
    }
}