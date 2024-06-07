import com.example.weedy.data.entities.Plant
import com.example.weedy.data.models.Genetic


object ExampleData {
    fun loadExamplePlants(): List<Plant> {
        return listOf(
            Plant(
                id = 2,
                isChecked = true,
                strain = "Amnesia Haze",
                genetic = Genetic(
                    0,
                    "Dinafem Seeds",
                    70,
                    30,
                    40,
                    "Feminized",
                    12
                ),
            ),
            Plant(
                id = 3,
                isChecked = false,
                strain = "White Widow",
                genetic = Genetic(
                    0,
                    "Dutch Passion",
                    60,
                    40,
                    50,
                    "Regular",
                    9
                )
            ),
            Plant(
                id = 4,
                isChecked = true,
                strain = "Northern Lights",
                genetic = Genetic(
                    0,
                    "Sensi Seeds",
                    90,
                    10,
                    45,
                    "Feminized",
                    7
                ),
            ),
            Plant(
                id = 5,
                isChecked = false,
                strain = "Super Silver Haze",
                genetic = Genetic(
                    0,
                    "Mr. Nice",
                    30,
                    70,
                    25,
                    "Regular",
                    11
                ),
            ),
            Plant(
                id = 6,
                isChecked = true,
                strain = "Girl Scout Cookies",
                genetic = Genetic(
                    0,
                    "Cali Connection",
                    40,
                    60,
                    28,
                    "Feminized",
                    9
                ),
            ),
            Plant(
                id = 7,
                isChecked = false,
                strain = "Sour Diesel",
                genetic = Genetic(
                    0,
                    "Humboldt Seeds",
                    30,
                    70,
                    53,
                    "Regular",
                    10
                ),
            ),
            Plant(
                id = 8,
                isChecked = true,
                strain = "Blue Dream",
                genetic = Genetic(
                    0,
                    "Humboldt Seeds",
                    30,
                    70,
                    63,
                    "Feminized",
                    9
                ),
            ),
            Plant(
                id = 9,
                isChecked = false,
                strain = "OG Kush",
                genetic = Genetic(
                    0,
                    "Dinafem Seeds",
                    75,
                    25,
                    19,
                    "Feminized",
                    8
                ),
            ),
            Plant(
                id = 10,
                isChecked = true,
                strain = "Granddaddy Purple",
                genetic = Genetic(
                    0,
                    "Barney's Farm",
                    100,
                    0,
                    17,
                    "Feminized",
                    8
                ),
            ),
            Plant(
                id = 11,
                isChecked = false,
                strain = "AK-47",
                genetic = Genetic(
                    0,
                    "Serious Seeds",
                    35,
                    65,
                    53,
                    "Regular",
                    7
                ),
            ),
            Plant(
                id = 12,
                isChecked = true,
                strain = "Bubble Gum",
                genetic = Genetic(
                    0,
                    "Serious Seeds",
                    55,
                    45,
                    52,
                    "Feminized",
                    8
                ),
            ),
            Plant(
                id = 13,
                isChecked = false,
                strain = "Cheese",
                genetic = Genetic(
                    0,
                    "Dinafem Seeds",
                    50,
                    50,
                    48,
                    "Feminized",
                    8
                ),
            ),
            Plant(
                id = 14,
                isChecked = true,
                strain = "Trainwreck",
                genetic = Genetic(
                    0,
                    "Greenhouse Seeds",
                    10,
                    90,
                    25,
                    "Feminized",
                    8
                ),
            ),
            Plant(
                id = 15,
                isChecked = false,
                strain = "Durban Poison",
                genetic = Genetic(
                    0,
                    "Dutch Passion",
                    100,
                    0,
                    23,
                    "Regular",
                    8
                ),
            ),
            Plant(
                id = 16,
                isChecked = true,
                strain = "Blueberry",
                genetic = Genetic(
                    0,
                    "DJ Short",
                    80,
                    20,
                    20,
                    "Regular",
                    9
                ),
            ),
        )
    }
}