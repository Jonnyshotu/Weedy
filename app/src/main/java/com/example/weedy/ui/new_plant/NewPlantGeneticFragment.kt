package com.example.weedy.ui.new_plant

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.RemoteGenetic
import com.example.weedy.data.entities.Soil
import com.example.weedy.databinding.FragmentNewPlantBinding
import com.example.weedy.databinding.FragmentNewPlantGeneticBinding


class NewPlantGeneticFragment : Fragment() {

    private val TAG = "New Plant Genetic Fragment"
    private lateinit var binding: FragmentNewPlantGeneticBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private val args: NewPlantGeneticFragmentArgs by navArgs()

    private lateinit var plant: MasterPlant


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlantGeneticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseID = args.databaseMasterPlantID
        Log.d(TAG, "Inserted plant ID: $databaseID")

        viewModel.getPlantByID(databaseID)

        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant
            binding.newPlantGeneticTitleTV.text = plant.strainName
        }

        binding.newPlantGeneticCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endGeneticDetail)
            binding.newPlantGeneticDetailsML.transitionToEnd()
        }

        binding.newPlantCannabinoidsCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endGeneticCanabinoids)
            binding.newPlantCannabinoidsML.transitionToEnd()

        }

        binding.newPlantGeneticML.setOnClickListener {
            binding.newPlantGeneticDetailsML.transitionToStart()
            binding.newPlantGeneticML.transitionToStart()
            binding.newPlantCannabinoidsML.transitionToStart()
        }


        var geneticType = "Unknown"
        binding.newPlantGeneticRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.newPlantRegularRB -> {
                    geneticType = binding.newPlantRegularRB.text.toString()
                }

                R.id.newPlantFeminizedRB -> {
                    geneticType = binding.newPlantFeminizedRB.text.toString()
                }

                R.id.newPlantAutomaticRB -> {
                    geneticType = binding.newPlantAutomaticRB.text.toString()
                }

                else -> {
                    geneticType = "Unknown"
                }
            }
        }

        binding.newPlantGeneticSaveBTN.setOnClickListener {

            with(plant) {
                breedingType = geneticType
                sativa = binding.newPlantGeneticSativaSB.progress.toString()
                indica = binding.newPlantGeneticIndicaSB.progress.toString()
                ruderalis = binding.newPlantGeneticRuderalisSB.progress.toString()
                // thc = binding.newPlantTHCET.text.toString()
                // cbd = binding.newPlantCBDET.text.toString()
            }
            updatePlant(plant)
            findNavController().navigate(
                NewPlantGeneticFragmentDirections.actionNewPlantGeneticFragmentToNewPlantStateFragment(
                    plant.id
                )
            )
        }
    }


    private fun updatePlant(plant: MasterPlant) {
        try {
            viewModel.updatePlant(plant)
            Toast.makeText(context, "Plant updated", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e(TAG, "Error updating plant", e)
            Toast.makeText(context, "An error occurred while updating plant", Toast.LENGTH_LONG)
                .show()
        }
    }
}