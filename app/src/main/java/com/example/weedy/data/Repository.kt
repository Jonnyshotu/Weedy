package com.example.weedy.data

import com.example.weedy.data.module.Genetic
import com.example.weedy.data.module.Nutrients
import com.example.weedy.data.module.Plant
import com.example.weedy.data.module.Soil
import java.time.LocalDate

object Repository {

    fun loadSoilTypes(): List<Soil>{
        return listOf(
            Soil("Light Mix BioBizz"),
            Soil("All Mix BioBizz" ),
            Soil("Coco BioBizz"),
        )
    }

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
    fun loadExamplePlants(): List<Plant> {
        return listOf(
            Plant(
                1,
                "Jack Herer",
                Genetic(40, 40, 40, "Automatic"),
                LocalDate.of(2024, 4, 14),
                "BioBizz Light Mix",
                LocalDate.of(2024, 4, 16),
                LocalDate.of(2024, 4, 18),
                12,
                false,
                20,
                manufacturer = "Royal Queen Seeds",
                watering = listOf(Pair(56.0,LocalDate.now()), Pair(89489.0,LocalDate.now()), Pair(4891.0,LocalDate.now()), Pair(87900.0,LocalDate.now()), Pair(305250.0,LocalDate.now()), Pair(5880.0,LocalDate.now()), Pair(566660.0,LocalDate.now()), Pair(20.0,LocalDate.now()), Pair(5600.0,LocalDate.now()), Pair(590.0,LocalDate.now()))
            ),
            Plant(
                2,
                "Blue Dream",
                Genetic(30, 60, 10, "Regular"),
                LocalDate.of(2024, 3, 1),
                "Canna Coco",
                LocalDate.of(2024, 3, 15),
                LocalDate.of(2024, 3, 20),
                10,
                false,
                60
            ),
            Plant(
                3,
                "Girl Scout Cookies",
                Genetic(50, 40, 10, "Feminized"),
                LocalDate.of(2024, 3, 10),
                "FoxFarm Ocean Forest",
                LocalDate.of(2024, 3, 20),
                LocalDate.of(2024, 3, 25),
                8,
                false,
                10
            ),
            Plant(
                4,
                "Green Crack",
                Genetic(40, 50, 10, "Automatic"),
                LocalDate.of(2024, 4, 5),
                "General Hydroponics Flora Series",
                LocalDate.of(2024, 4, 15),
                LocalDate.of(2024, 4, 20),
                9,
                false,
                50
            ),
            Plant(
                5,
                "Northern Lights",
                Genetic(20, 70, 10, "Regular"),
                LocalDate.of(2024, 4, 1),
                "Advanced Nutrients pH Perfect",
                LocalDate.of(2024, 4, 15),
                LocalDate.of(2024, 4, 20),
                11,
                false,
                2
            ),
            Plant(
                6,
                "OG Kush",
                Genetic(50, 40, 10, "Feminized"),
                LocalDate.of(2024, 4, 10),
                "Canna Aqua Vega",
                LocalDate.of(2024, 4, 20),
                LocalDate.of(2024, 4, 25),
                7,
                false,
                95
            ),
            Plant(
                7,
                "Amnesia Haze",
                Genetic(60, 30, 10, "Regular"),
                LocalDate.of(2024, 4, 5),
                "Coco Bliss",
                LocalDate.of(2024, 4, 15),
                LocalDate.of(2024, 4, 20),
                12,
                false,
                45
            ),
            Plant(
                8,
                "White Widow",
                Genetic(40, 50, 10, "Feminized"),
                LocalDate.of(2024, 4, 10),
                "Canna Terra Vega",
                LocalDate.of(2024, 4, 20),
                LocalDate.of(2024, 4, 25),
                9,
                false,
                80
            ),
            Plant(
                9,
                "Critical Mass",
                Genetic(20, 70, 10, "Automatic"),
                LocalDate.of(2024, 4, 5),
                "Canna Aqua Flores",
                LocalDate.of(2024, 4, 15),
                LocalDate.of(2024, 4, 20),
                11,
                false,
                0
            )
        )
    }
}