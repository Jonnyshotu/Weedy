package com.example.weedy.ui.new_plant

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
import com.example.weedy.data.models.actions.RepotAction
import com.example.weedy.data.models.record.GrowthStateRecord
import com.example.weedy.databinding.FragmentNewPlantStateBinding
import java.time.LocalDate

class NewPlantStateFragment : Fragment() {

    private val TAG = "New Plant State Fragment"
    private lateinit var binding: FragmentNewPlantStateBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private val args: NewPlantStateFragmentArgs by navArgs()

    private val databaseID = args.databaseMasterPlantID
    private lateinit var plant: MasterPlant
    private var soilList: List<Soil>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.getPlantByID(databaseID)

        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant
        }

        viewModel.soilList.observe(viewLifecycleOwner) { soilList ->
            this.soilList = soilList
        }

        binding = FragmentNewPlantStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var growthState = 0
        binding.newPlantgrowthStateRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.seedling_radio -> {
                    growthState = 1
                }

                R.id.cutting_radio -> {
                    growthState = 2
                }

                R.id.veg_radio -> {
                    growthState = 3
                }

                R.id.flower_radio -> {
                    growthState = 4
                }

                else -> {
                    growthState = 0
                }
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

        binding.newPlantStateSaveBTN.setOnClickListener {
            val potSize = binding.newPlantPotSizeET.text.toString().toDouble()

            if (soil != null) {

                val repot = RepotAction(0, plant.id, soil!!.id, potSize, LocalDate.now())
                val state = GrowthStateRecord(0, plant.id, growthState, LocalDate.now())

                insertState(repot, state)
            }
        }
    }

    fun insertState(repotAction: RepotAction, state: GrowthStateRecord) {
        try {
            viewModel.insertRepot(repotAction)
            Toast.makeText(context, "Soil inserted", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e(TAG, "Error updating plant", e)
            Toast.makeText(context, "An error occurred while inserting soil", Toast.LENGTH_LONG)
                .show()
        }
        try {
            viewModel.insertGrowthState(state)
            Toast.makeText(context, "State inserted", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e(TAG, "Error inserting state ", e)
            Toast.makeText(context, "An error occurred while inserting state", Toast.LENGTH_LONG)
                .show()
        }

    }
}