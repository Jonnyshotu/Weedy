package com.example.weedy.ui.new_plant

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.adapter.list.ListStringAdapter
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.RemoteGenetic
import com.example.weedy.databinding.FragmentNewPlantBinding
import com.example.weedy.ui.main.MainFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewPlantFragment : MainFragment() {

    private val TAG = "New Plant Fragment" // Tag for logging
    private lateinit var binding: FragmentNewPlantBinding // Binding object for the fragment's layout
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for data

    private var remoteGeneticList: List<RemoteGenetic>? = null // List of remote genetic data
    private var localGeneticList: List<LocalGenetic>? = null // List of local genetic data
    private var debouncedTextWatcher: TextWatcher? = null // TextWatcher for debounced text changes

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlantBinding.inflate(inflater, container, false) // Inflate the layout
        return binding.root // Return the root view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe changes in the remote genetic collection
        viewModel.remoteGeneticCollection.observe(viewLifecycleOwner) { remoteGeneticList ->
            this.remoteGeneticList = remoteGeneticList // Update remote genetic list
            Log.d(TAG, "Remote genetic list size: ${remoteGeneticList.size}")
        }

        // Observe changes in the local genetic collection
        viewModel.localGeneticCollection.observe(viewLifecycleOwner) { localGeneticList ->
            this.localGeneticList = localGeneticList // Update local genetic list
            Log.d(TAG, "Local genetic list size: ${localGeneticList.size}")
        }

        // Setup TextWatcher for strain input
        setupStrainInputTextWatcher()

        // Set click listener for save button
        binding.newPlantSaveBTN.setOnClickListener {
            val remoteGenetic = checkRemoteEntry() // Check remote entry
            val localGenetic = checkLocalEntry(remoteGenetic?.strainName) // Check local entry
            savePlant(localGenetic?.id, remoteGenetic?.stainOCPC) // Save plant data
        }
    }

    /**
     * Inserts the created Master_Plant to the database
     * @param localGeneticID if a match is found in checkLocalEntry()
     * @param remoteGeneticID if a match is found in checkRemoteEntry()
     */
    private fun savePlant(localGeneticID: Long?, remoteGeneticID: String?) {
        try {
            // Insert plant into database
            viewModel.insertPlant(
                MasterPlant(
                    id = 0, // Plant ID
                    strainName = binding.newPlantStrainET.text.toString(), // Strain name
                    seedCompany = binding.newPlantSeedCompanyEt.text.toString(), // Seed company
                    localGeneticID = localGeneticID, // Local genetic ID
                    remoteGeneticID = remoteGeneticID, // Remote genetic ID
                )
            ) { databaseID ->
                Toast.makeText(context, "Saved!", Toast.LENGTH_LONG).show() // Show success message
                Log.d(TAG, "Inserted plant ID: $databaseID") // Log the inserted plant ID
                findNavController().navigate(
                    NewPlantFragmentDirections.actionNewPlantFragmentToNewPlantGeneticFragment(
                        databaseID
                    )
                ) // Navigate to the next fragment
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting plant", e) // Log error
            Toast.makeText(context, "An error occurred while saving...", Toast.LENGTH_LONG)
                .show() // Show error message
        }
    }

    /**
     * Checks if the input matches the local data.
     * @return a Local_Genetic if a match is found
     */
    private fun checkLocalEntry(strain: String?): LocalGenetic? {
        // Check if the strain exists in the local genetic list
        val result = localGeneticList?.find { it.strainName.equals(strain, ignoreCase = true) }
        Log.d(TAG, "Local entry check: $result") // Log the result
        return result // Return the matching local genetic
    }

    /**
     * Checks if the input matches data from API.
     * @return a Remote_Genetic if a match is found
     */
    private fun checkRemoteEntry(): RemoteGenetic? {
        val strain = binding.newPlantStrainET.text.toString().trim() // Strain from input
        val company =
            binding.newPlantSeedCompanyEt.text.toString().trim() // Seed company from input

        // Check if the strain and company exist in the remote genetic list
        val result = remoteGeneticList?.find {
            it.strainName.equals(
                strain,
                ignoreCase = true
            ) && it.seedCompany.equals(company, ignoreCase = true)
        }
        Log.d(TAG, "Database entry check: $result") // Log the result
        return result // Return the matching remote genetic
    }

    /**
     * Sets a debounced text watcher with 300 ms
     */
    private fun setupStrainInputTextWatcher() {
        debouncedTextWatcher = object : TextWatcher {
            private var debounceJob: Job? = null // Job for debouncing text changes

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Debounce user input to avoid too many RV updates
                debounceJob?.cancel()
                debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    updateStrainRV(s.toString()) // Update strain RecyclerView
                    updateCompanyRV(s.toString()) // Update company RecyclerView
                }
            }
        }
        binding.newPlantStrainET.addTextChangedListener(debouncedTextWatcher) // Add TextWatcher to strain input
    }

    /**
     * Updates the strain RV.
     * @param input is the strain ET
     */
    private fun updateStrainRV(input: String) {
        if (input.isNotBlank()) {
            // Filter remote genetic list based on input
            val filter = remoteGeneticList?.filter { genetic ->
                genetic.strainName.contains(input, ignoreCase = true)
            }
            if (filter != null) {
                Log.d(TAG, "Filtered list size: ${filter.size}") // Log filtered list size
            }

            if (!filter.isNullOrEmpty()) {
                val strainNames =
                    listOf("Select strain from database") + filter.map { it.strainName }

                Log.d(TAG, "Filtered strain names: $strainNames") // Log filtered strain names

                // Update RecyclerView with filtered strain names
                val adapter = ListStringAdapter(strainNames) { selectedString ->
                    binding.newPlantStrainET.setText(selectedString) // Set selected strain name
                }
                binding.newPlantStrainRV.adapter = adapter // Set adapter to RecyclerView
                binding.newPlantML.transitionToState(R.id.endInput) // Transition to end input state
            }
        }
    }

    /**
     * Updates the seed company RV.
     * @param input is the strain ET
     */
    private fun updateCompanyRV(input: String) {
        if (input.isNotBlank()) {
            val filter = remoteGeneticList?.filter { genetic ->
                genetic.strainName.contains(input, ignoreCase = true)
            }

            if (!filter.isNullOrEmpty()) {
                var companyNames = filter.map { it.seedCompany }

                // Update RecyclerView with filtered seed company names
                var adapter = ListStringAdapter(companyNames) { selectedString ->
                    binding.newPlantSeedCompanyEt.setText(selectedString) // Set selected seed company
                    binding.newPlantML.transitionToState(R.id.endContinue) // Transition to end continue state
                }
                binding.newPlantSeedCompanyRV.adapter = adapter // Set adapter to RecyclerView

                // Handle text changes in the seed company field
                binding.newPlantSeedCompanyEt.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        Log.d(
                            TAG,
                            "Seed Company ET afterTextChanged: $s"
                        ) // Log seed company text change
                        if (!s.isNullOrBlank()) {
                            binding.newPlantML.transitionToState(R.id.endContinue) // Transition to end continue state
                            companyNames =
                                listOf("Select seed company from database") + companyNames.filter { companyName ->
                                    companyName?.contains(s.toString(), ignoreCase = true) ?: false
                                }

                        } else {
                            companyNames = emptyList()
                        }
                        // Update RecyclerView with filtered seed company names
                        adapter = ListStringAdapter(companyNames) { selectedString ->
                            binding.newPlantSeedCompanyEt.setText(selectedString) // Set selected seed company
                        }
                        binding.newPlantSeedCompanyRV.adapter =
                            adapter // Set adapter to RecyclerView
                    }
                })
            }
        }
    }

    /**
     * Sets the captured image to the ImageView
     * @param imageBitmap the captured image
     */
    override fun onImageCaptured(imageBitmap: Bitmap) {
        // Set captured image to ImageView
        binding.newPlantPlantIV.setImageBitmap(imageBitmap)
    }

    /**
     * Sets the picked image to the ImageView
     * @param imageUri the URI of the picked image
     */
    override fun onImagePicked(imageUri: Uri?) {
        // Set picked image to ImageView
        binding.newPlantPlantIV.setImageURI(imageUri)
    }
}
