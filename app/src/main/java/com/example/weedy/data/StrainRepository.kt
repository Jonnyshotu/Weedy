package com.example.weedy.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weedy.data.entities.Genetic
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.remote.StrainApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class StrainRepository(private val database: PlantDatabase) {

    private val TAG = "StrainRepository"

    var geneticCollection = database.plantDao.getAllGenetics()

    suspend fun insertGenetic(genetic: Genetic) = database.plantDao.insertGenetic(genetic)

    suspend fun fetchStrains() {
        withContext(Dispatchers.IO) {
            var currentPage = 1
            var totalPages = 1

            do {
                try {
                    val response = StrainApi.apiService.getStrainResponse(currentPage)
                    Log.d(TAG, "Response: $response")

                    val strains = response.data
                    Log.d(TAG, "Strains: $strains")

                    Log.d(TAG, "Fetched ${strains.size} strains")

                    if (strains.isNotEmpty()) {
                        val geneticList = strains.map { strain ->
                            Genetic(
                                id = 0,
                                strainName = strain.name,
                                stainOCPC = strain.ocpc,
                                seedCompany = strain.seedCompany?.name ?: "",
                                seedCompanyOCPC = strain.seedCompany?.ocpc ?: "",
                                parentOCPC = strain.genetics?.ocpc.toString(),
                                parentNames = strain.genetics?.names.toString(),
                                sativa = null,
                                indica = null,
                                ruderalis = null,
                                breedingType = null,
                                floweringTime = null
                            )
                        }

                        Log.d(TAG, "Prepared genetics for database insertion $geneticList")

                        database.plantDao.insertGeneticList(geneticList)

                        Log.d(
                            TAG,
                            "Genetics in database after insertion: $geneticCollection"
                        )

                        totalPages = response.meta.pagination.total_pages
                    } else {
                        Log.d(TAG, "No strains fetched from API")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error fetching strains", e)
                    delay(35000) // Api timeout
                }

                currentPage++
            } while (currentPage <= totalPages)
        }
    }
}
