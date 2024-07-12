package com.example.weedy

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.weedy.data.StrainRepository
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.local.getDatabase
import kotlin.math.min

class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "ExploreViewModel"

    private val database = getDatabase(application)
    private val strainRepository = StrainRepository(database, application.applicationContext)
    val localGeneticCollection = strainRepository.localGeneticCollection

    private val _searchInput = MutableLiveData<String?>()
    private val _geneticType = MutableLiveData<String?>()
    private val _minThc = MutableLiveData<Float?>()

    val filteredGeneticList: LiveData<List<LocalGenetic>> =
        MediatorLiveData<List<LocalGenetic>>().apply {
            addSource(localGeneticCollection) { updateFilteredList() }
            addSource(_searchInput) { updateFilteredList() }
            addSource(_geneticType) { updateFilteredList() }
            addSource(_minThc) { updateFilteredList() }
        }

    private fun updateFilteredList() {
        val searchInput = _searchInput.value
        val geneticType = _geneticType.value
        val minThc = _minThc.value
        val list = localGeneticCollection.value ?: emptyList()

        Log.d(TAG,"Search input: $searchInput, genetic type: $geneticType, minThc: $minThc, local genetic list size: ${list.size}")

        (filteredGeneticList as MediatorLiveData).value = list.filter { genetic ->
            (searchInput.isNullOrBlank() || genetic.strainName.contains(
                searchInput,
                ignoreCase = true
            )) &&
                    (geneticType.isNullOrBlank() || genetic.strainType == geneticType) &&
                    (minThc == null || minThc == 0f || (parseTHCLevel(genetic.thcLevel)
                        ?: 0f) >= minThc)
        }
    }

    fun updateSearchInput(input: String?) {
        _searchInput.value = input
    }

    fun updateGeneticType(type: String?) {
        _geneticType.value = type
    }

    fun updateMinThc(thc: Float?) {
        _minThc.value = thc
    }

    private fun parseTHCLevel(thcLevel: String?): Float? {
        return thcLevel?.removeSuffix("%")?.toFloatOrNull()
    }
}