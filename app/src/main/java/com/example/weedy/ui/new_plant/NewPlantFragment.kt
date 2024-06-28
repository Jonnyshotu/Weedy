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

    private val TAG = "New Plant Fragment"
    private lateinit var binding: FragmentNewPlantBinding
    private val viewModel: SharedViewModel by activityViewModels()

    private var remoteGeneticList: List<RemoteGenetic>? = null
    private var localGeneticList: List<LocalGenetic>? = null
    private var debouncedTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe changes in the remote genetic collection
        viewModel.remoteGeneticCollection.observe(viewLifecycleOwner) { remoteGeneticList ->
            this.remoteGeneticList = remoteGeneticList
            Log.d(TAG, "Remote genetic list size: ${remoteGeneticList.size}")
        }

        // Observe changes in the local genetic collection
        viewModel.localGeneticCollection.observe(viewLifecycleOwner) { localGeneticList ->
            this.localGeneticList = localGeneticList
            Log.d(TAG, "Local genetic list size: ${localGeneticList.size}")
        }

        // Setup TextWatcher for strain input
        setupStrainInputTextWatcher()

        // Set click listener for save button
        binding.newPlantSaveBTN.setOnClickListener {
            val remoteGenetic = checkRemoteEntry()
            val localGenetic = checkLocalEntry(remoteGenetic?.strainName)

            savePlant(localGenetic?.id, remoteGenetic?.stainOCPC)
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
                    id = 0,
                    strainName = binding.newPlantStrainET.text.toString(),
                    seedCompany = binding.newPlantSeedCompanyEt.text.toString(),
                    localGeneticID = localGeneticID,
                    remoteGeneticID = remoteGeneticID,
                )
            ) { databaseID ->
                Toast.makeText(context, "Saved!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Inserted plant ID: $databaseID")
                findNavController().navigate(
                    NewPlantFragmentDirections.actionNewPlantFragmentToNewPlantGeneticFragment(
                        databaseID
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting plant", e)
            Toast.makeText(context, "An error occurred while saving...", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Checks if the input matches the local data.
     * @return a Local_Genetic if a match is found
     */
    private fun checkLocalEntry(strain: String?): LocalGenetic? {

        // Check if the strain exists in the local genetic list
        val result = localGeneticList?.find { it.strainName == strain }
        Log.d(TAG, "Local entry check: $result")
        return result
    }


    /**
     * Checks if the input matches data from api.
     * @return a Remote_Genetic if a match is found
     */
    private fun checkRemoteEntry(): RemoteGenetic? {

        val strain = binding.newPlantStrainET.text.toString()
        val company = binding.newPlantSeedCompanyEt.text.toString()

        // Check if the strain and company exist in the remote genetic list
        val result =
            remoteGeneticList?.find { it.strainName == strain && it.seedCompany == company }
        Log.d(TAG, "Database entry check: $result")
        return result
    }


    /**
     * Sets a debounced text watcher with 300 mils
     */
    private fun setupStrainInputTextWatcher() {

        debouncedTextWatcher = object : TextWatcher {
            private var debounceJob: Job? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Debounce user input to avoid to many RV updates
                debounceJob?.cancel()
                debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    updateStrainRV(s.toString())
                    updateCompanyRV(s.toString())
                }
            }
        }
        binding.newPlantStrainET.addTextChangedListener(debouncedTextWatcher)
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
                Log.d(TAG, "Filtered list size: ${filter.size}")
            }

            if (!filter.isNullOrEmpty()) {
                val strainNames =
                    listOf("Select strain from database") + filter.map { it.strainName }

                Log.d(TAG, "Filtered strain names: $strainNames")

                // Update RecyclerView with filtered strain names
                val adapter = ListStringAdapter(strainNames) { selectedString ->
                    binding.newPlantStrainET.setText(selectedString)
                }
                binding.newPlantStrainRV.adapter = adapter
                binding.newPlantML.transitionToState(R.id.endInput)
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
                    binding.newPlantSeedCompanyEt.setText(selectedString)
                    binding.newPlantML.transitionToState(R.id.endContinue)

                }
                binding.newPlantSeedCompanyRV.adapter = adapter

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
                        Log.d(TAG, "Seed Company ET afterTextChanged: $s")
                        if (!s.isNullOrBlank()) {
                            companyNames =
                                listOf("Select seed company from database") + companyNames.filter { companyName ->
                                    companyName?.contains(s.toString(), ignoreCase = true) ?: false
                                }
                            if (!binding.newPlantStrainET.text.isNullOrBlank()) {
                                binding.newPlantML.transitionToState(R.id.endContinue)
                            }
                        } else {
                            companyNames = emptyList()
                        }
                        // Update RecyclerView with filtered strain names
                        adapter = ListStringAdapter(companyNames) { selectedString ->
                            binding.newPlantSeedCompanyEt.setText(selectedString)
                        }
                        binding.newPlantSeedCompanyRV.adapter = adapter
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
