package com.example.weedy.ui.new_plant

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.models.actions.GerminationAction
import com.example.weedy.data.models.actions.PlantedAction
import com.example.weedy.data.models.record.RepotRecord
import com.example.weedy.data.models.record.GrowthStateRecord
import com.example.weedy.databinding.FragmentNewPlantStateBinding
import com.example.weedy.ui.main.MainFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Fragment for managing the state of a new plant, including setting growth state, soil, and pot size.
 */
class NewPlantStateFragment : MainFragment() {

    private val TAG = "New Plant State Fragment" // Tag for logging
    private lateinit var binding: FragmentNewPlantStateBinding // View binding for this fragment
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for this fragment
    private val args: NewPlantStateFragmentArgs by navArgs() // Arguments passed to this fragment

    private lateinit var plant: MasterPlant // Current plant being managed
    private var soilList: List<Soil>? = null // List of available soils
    private var selectedDate = LocalDate.now() // Default date set to today
    private val dateFormatter =
        DateTimeFormatter.ofPattern("dd.MM.yyyy") // Date formatter for display


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlantStateBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseID = args.databaseMasterPlantID // Plant ID passed via arguments

        // Get current date for the date picker dialog
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR) // Current year
        val month = calendar.get(Calendar.MONTH) // Current month
        val day = calendar.get(Calendar.DAY_OF_MONTH) // Current day

        binding.newPlantStateDateTV.text =
            selectedDate.format(dateFormatter) // Set initial date text

        // Fetch plant data by ID
        viewModel.getPlantByID(databaseID)
        Log.d(TAG, "Navigation plant ID: $databaseID") // Log plant ID

        // Observe plant data and update UI
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant
            binding.newPlantStateStrainTV.text = plant.strainName // Display plant strain
        }

        // Load and setup soil list for the spinner
        viewModel.soilList.observe(viewLifecycleOwner) { soils ->
            soilList = soils
            binding.newPlantSpinnerPotSize.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                soilList?.map { it.name } ?: emptyList() // Populate spinner with soil names
            )
        }

        var growthState = 0 // Variable to track growth state
        binding.newPlantgrowthStateRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.newPlantgrowthStateGermination_radio -> {
                    growthState = 1 // Set growth state to Germination
                    animateViewToGone(binding.newPlantStateSoilCV) // Hide soil view
                }

                R.id.newPlantgrowthStateSeedling_radio -> {
                    growthState = 2 // Set growth state to Seedling
                    if (!binding.newPlantStateSoilCV.isVisible) animateViewToVisible(binding.newPlantStateSoilCV) // Show soil view
                }

                R.id.newPlantgrowthStateCutting_radio -> {
                    growthState = 3 // Set growth state to Cutting
                    if (!binding.newPlantStateSoilCV.isVisible) animateViewToVisible(binding.newPlantStateSoilCV) // Show soil view
                }

                R.id.newPlantgrowthStateVeg_radio -> {
                    growthState = 4 // Set growth state to Vegetative
                    if (!binding.newPlantStateSoilCV.isVisible) animateViewToVisible(binding.newPlantStateSoilCV) // Show soil view
                }

                R.id.newPlantgrowthStateFlower_radio -> {
                    growthState = 5 // Set growth state to Flowering
                    if (!binding.newPlantStateSoilCV.isVisible) animateViewToVisible(binding.newPlantStateSoilCV) // Show soil view
                }

                else -> growthState = 0 // Default to no growth state
            }
        }

        var soil: Soil? = null // Selected soil
        binding.newPlantSpinnerPotSize.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) { // Ignore the first (empty) item
                        val selectedSoil = parent.getItemAtPosition(position) as String
                        soil = soilList?.find { it.name == selectedSoil } // Find selected soil
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        // Handle Enter button click
        binding.newPlantStateEnterBTN.setOnClickListener {
            binding.newPlantStateML.transitionToEnd() // Transition to end state of motion layout
        }

        // Handle Skip button click
        binding.newPlantStateSkipBTN.setOnClickListener {
            findNavController().navigate(R.id.homeFragment) // Navigate to home fragment
        }

        // Show date picker dialog on date text view click
        binding.newPlantStateDateTV.setOnClickListener {
            showDatePickerDialog(year, month, day)
        }

        var potSize: Double? = null // Variable to store pot size
        binding.newPlantPotSizeET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                try {
                    potSize = s.toString().toDouble() // Convert text to double
                    binding.newPlantPotSizeET.error = null // Clear error if valid
                } catch (e: NumberFormatException) {
                    binding.newPlantPotSizeET.error = "Invalid pot size" // Show error if invalid
                }
            }
        })

        // Handle Save button click
        binding.newPlantStateSaveBTN.setOnClickListener {
            if (growthState == 1) {
                // If growth state is Germination
                val state = GrowthStateRecord(0, plant.id, growthState, selectedDate)
                val germination = GerminationAction(0, plant.id, null, selectedDate, null)

                insertState(repot = null, state = state, planted = null, germination = germination)

                findNavController().navigate(R.id.homeFragment) // Navigate to home fragment

            } else if (growthState in 2..5 && soil != null && potSize != null && selectedDate != null) {
                // If growth state is between Seedling and Flowering
                val planted = PlantedAction(0, plant.id, soil!!.id, soil!!.name, selectedDate)
                val repot =
                    RepotRecord(0, plant.id, soil!!.id, soil!!.name, potSize!!, selectedDate)
                val state = GrowthStateRecord(0, plant.id, growthState, selectedDate)

                insertState(repot = repot, state = state, planted = planted, germination = null)

                findNavController().navigate(R.id.homeFragment) // Navigate to home fragment

            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_LONG)
                    .show() // Show error if fields are missing
            }
        }
    }

    /**
     * Inserts the state information into the database.
     */
    private fun insertState(
        repot: RepotRecord?,
        state: GrowthStateRecord,
        planted: PlantedAction?,
        germination: GerminationAction?
    ) {
        try {
            germination?.let {
                viewModel.insertGermination(it) // Insert germination action
                Log.d(TAG, "Germination inserted: $it")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting germination", e)
        }

        try {
            planted?.let {
                viewModel.insertPlanted(it) // Insert planted action
                Log.d(TAG, "Planted inserted: $it")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting planted", e)
        }

        try {
            repot?.let {
                viewModel.insertRepot(it) // Insert repot record
                Log.d(TAG, "Repot inserted: $it")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting repot", e)
        }

        try {
            viewModel.insertGrowthState(state) // Insert growth state record
            Log.d(TAG, "Growth state inserted: $state")
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting state", e)
        }
    }

    /**
     * Shows a date picker dialog to select a date.
     */
    private fun showDatePickerDialog(year: Int, month: Int, day: Int) {
        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                binding.newPlantStateDateTV.text =
                    selectedDate.format(dateFormatter) // Update date text
            },
            year, month, day
        ).show()
    }

    override fun onImageCaptured(imageBitmap: Bitmap) {
        // No implementation needed for image capture in this fragment
    }

    override fun onImagePicked(imageUri: Uri?) {
        // No implementation needed for image picking in this fragment
    }
}
