package com.example.weedy

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.weedy.data.StrainRepository
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.local.getDatabase

/**
 * ViewModel for exploring and filtering genetic strains based on various criteria.
 */
class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "ExploreViewModel"

    private val database = getDatabase(application) // Database instance for accessing data
    private val strainRepository = StrainRepository(database, application.applicationContext) // Repository for strain data
    private val localGeneticCollection = strainRepository.localGeneticCollection // LiveData of the local genetic collection

    private val _searchInput = MutableLiveData<String?>() // LiveData for search input
    private val _geneticType = MutableLiveData<String?>() // LiveData for genetic type filter
    private val _minThc = MutableLiveData<Float?>() // LiveData for minimum THC level filter

    //LiveData that provides a filtered list of genetic strains based on the current filters.
    val filteredGeneticList: LiveData<List<LocalGenetic>> =
        MediatorLiveData<List<LocalGenetic>>().apply {
            // Add sources to the MediatorLiveData
            addSource(localGeneticCollection) { updateFilteredList() }
            addSource(_searchInput) { updateFilteredList() }
            addSource(_geneticType) { updateFilteredList() }
            addSource(_minThc) { updateFilteredList() }
        }

    /**
     * Updates the filtered list based on the current values of filters.
     */
    private fun updateFilteredList() {
        val searchInput = _searchInput.value
        val geneticType = _geneticType.value
        val minThc = _minThc.value
        val list = localGeneticCollection.value ?: emptyList()

        Log.d(
            TAG,
            "Search input: $searchInput, genetic type: $geneticType, minThc: $minThc, local genetic list size: ${list.size}"
        )

        (filteredGeneticList as MediatorLiveData).value = list.filter { genetic ->
            (searchInput.isNullOrBlank() || genetic.strainName.contains(
                searchInput,
                ignoreCase = true
            )) &&
                    (geneticType.isNullOrBlank() || genetic.strainType == geneticType) &&
                    (minThc == null || minThc == 0f || (parseTHCLevel(genetic.thcLevel)
                        ?: 0f) == minThc)
        }
    }

    /**
     * Resets all filters to their default values.
     */
    fun resetFilter() {
        _searchInput.value = null
        _geneticType.value = null
        _minThc.value = null
    }

    /**
     * Updates the search input filter.
     * @param input Search input.
     */
    fun updateSearchInput(input: String?) {
        _searchInput.value = input
    }

    /**
     * Updates the genetic type filter.
     * @param type Breeding type.
     */
    fun updateGeneticType(type: String?) {
        _geneticType.value = type
    }

    /**
     * Updates the minimum THC level filter.
     * @param thc THC value.
     */
    fun updateMinThc(thc: Float?) {
        _minThc.value = thc
    }

    /**
     * Parses a THC level from a string to a float.
     * @param thcLevel THC level as a string with optional '%' suffix.
     * @return Parsed THC level as a float or null if parsing fails.
     */
    private fun parseTHCLevel(thcLevel: String?): Float? {
        return thcLevel?.removeSuffix("%")?.toFloatOrNull()
    }
}
