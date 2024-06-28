package com.example.weedy.data

import android.content.Context
import android.util.Log
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.entities.RemoteGenetic
import com.example.weedy.data.local.PlantDatabase
import com.example.weedy.data.models.strains.LocalStrain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StrainRepository(private val database: PlantDatabase, private val context: Context) {

    private val TAG = "StrainRepository"

    var localGeneticCollection = database.localGeneticDao.getAllGenetics()
    var remoteGeneticCollection = database.remoteGeneticDao.getAllGenetics()


    suspend fun getRemoteStrains() {

        withContext(Dispatchers.IO) {

            var currentPage = 1
            var totalPages = 1

            do {
                try {

                    val response = StrainApi.apiService.getRemoteStrainResponse(currentPage)
                    Log.d(TAG, "Response: $response")

                    val strains = response.data
                    Log.d(TAG, "Fetched ${strains.size} strains")

                    if (strains.isNotEmpty()) {
                        val remoteGeneticList = strains.map { strain ->
                            RemoteGenetic(
                                stainOCPC = strain.ocpc,
                                strainName = strain.name,
                                seedCompany = strain.seedCompany.name ?: "",
                                seedCompanyOCPC = strain.seedCompany.ocpc ?: "",
                                parentOCPC = strain.genetics?.ocpc.toString(),
                                parentNames = strain.genetics?.names.toString(),
                                children = strain.children.toString(),
                                lineage = strain.lineage.toString(),
                            )
                        }

                        database.remoteGeneticDao.insertGeneticList(remoteGeneticList)

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

    suspend fun getLocalStrains() {
        withContext(Dispatchers.IO) {

            try {
                val gson = Gson()
                val inputStream = context.assets.open("leafly_strain_data.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                val type = object : TypeToken<List<LocalStrain>>() {}.type
                val strains = gson.fromJson<List<LocalStrain>>(jsonString, type)


                Log.d(TAG, "Local strains count: ${strains.size} strains")

                if (strains.isNotEmpty()) {
                    val localGeneticList = strains.map { strain ->
                        LocalGenetic(
                            id = 0,
                            strainName = strain.name,
                            strainImageURL = strain.img_url,
                            strainType = strain.type,
                            thcLevel = strain.thc_level,
                            description = strain.description,
                            mostCommonTerpene = strain.most_common_terpene,
                            effects = strain.effects.toString()
                        )
                    }

                    database.localGeneticDao.insertGeneticList(localGeneticList)

                } else {
                    Log.d(TAG, "No strains loaded from local")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading local strains", e)
            }
        }
    }
}