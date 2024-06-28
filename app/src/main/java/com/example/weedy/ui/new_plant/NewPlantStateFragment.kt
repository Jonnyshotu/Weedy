package com.example.weedy.ui.new_plant

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.models.actions.PlantedAction
import com.example.weedy.data.models.actions.RepotAction
import com.example.weedy.data.models.record.GrowthStateRecord
import com.example.weedy.databinding.FragmentNewPlantStateBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class NewPlantStateFragment : Fragment() {

    private val TAG = "New Plant State Fragment"
    private lateinit var binding: FragmentNewPlantStateBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private val args: NewPlantStateFragmentArgs by navArgs()

    private lateinit var plant: MasterPlant
    private var soilList: List<Soil>? = null
    private var selectedDate = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

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

        val databaseID = args.databaseMasterPlantID

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.newPlantStateDateTV.text = selectedDate?.format(dateFormatter)

        viewModel.getPlantByID(databaseID)
        Log.d(TAG, "Navigation plant ID: $databaseID")

        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant
            binding.newPlantStateStrainTV.text = plant.strainName
        }

        viewModel.soilList.observe(viewLifecycleOwner) { soils ->
            soilList = soils
            binding.newPlantSpinnerPotSize.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                soilList?.map { it.name } ?: emptyList()
            )
        }

        var growthState = 0
        binding.newPlantgrowthStateRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.seedling_radio -> growthState = 1
                R.id.cutting_radio -> growthState = 2
                R.id.veg_radio -> growthState = 3
                R.id.flower_radio -> growthState = 4
                else -> growthState = 0
            }
        }

        var soil: Soil? = null
        binding.newPlantSpinnerPotSize.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        val selectedSoil = parent.getItemAtPosition(position) as String
                        soil = soilList?.find { soil -> soil.name == selectedSoil }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        binding.newPlantStateEnterBTN.setOnClickListener {
            binding.newPlantStateML.transitionToEnd()
        }

        binding.newPlantStateSkipBTN.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.newPlantStateDateTV.setOnClickListener {
            showDatePickerDialog(year, month, day)
        }

        binding.newPlantStateSaveBTN.setOnClickListener {

            var potSize: Double? = null

            try {
                potSize = binding.newPlantPotSizeET.text.toString().toDouble()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Invalid pot size", Toast.LENGTH_LONG).show()
            }

            if (soil != null && potSize != null && selectedDate != null) {
                val planted = PlantedAction(0, plant.id, soil!!.id, selectedDate!!)
                val repot = RepotAction(0, plant.id, soil!!.id, potSize, selectedDate!!)
                val state = GrowthStateRecord(0, plant.id, growthState, selectedDate!!)

                insertState(repot, state, planted)

                findNavController().navigate(R.id.homeFragment)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun insertState(repot: RepotAction, state: GrowthStateRecord, planted: PlantedAction) {
        try {
            viewModel.insertPlanted(planted)
            Log.d(TAG, "Planted inserted: $planted")
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting planted", e)
        }
        try {
            viewModel.insertRepot(repot)
            Log.d(TAG, "Repot inserted: $repot")

        } catch (e: Exception) {
            Log.e(TAG, "Error updating plant", e)
        }
        try {
            viewModel.insertGrowthState(state)
            Log.d(TAG, "Growth state inserted: $state")
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting state ", e)
        }
    }

    private fun showDatePickerDialog(year: Int, month: Int, day: Int) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                binding.newPlantStateDateTV.text = selectedDate?.format(dateFormatter)
            }, year, month, day
        )
        datePickerDialog.show()
    }
}
