package com.example.weedy.ui.new_plant

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentNewPlantGeneticBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        // Get the plant ID passed via navigation
        val databaseID = args.databaseMasterPlantID
        Log.d(TAG, "Navigation plant ID: $databaseID")

        // Retrieve the plant data by ID
        viewModel.getPlantByID(databaseID)

        // Observe the plant data
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant
            binding.newPlantGeneticStrainTV.text = plant.strainName
        }

        var thc: Int? = null
        var cbd: Int? = null
        var flowertime: Int? = null
        var sativa: Int? = null
        var indica: Int? = null
        var ruderalis: Int? = null
        var geneticType: String? = null

        // Setup THC SeekBar listener
        binding.newPlantTHCSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thc = progress
                binding.newPlantTHCResultTV.text = "$thc %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Setup CBD SeekBar listener
        binding.newPlantCBDSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cbd = progress
                binding.newPlantCBDResultTV.text = "$cbd %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Setup Flowertime SeekBar listener
        binding.newPlantFlowertimeSB.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                flowertime = progress
                binding.newPlantFlowertimeDisplayTV.text = "$flowertime Weeks"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Setup Sativa SeekBar listener
        binding.newPlantGeneticSativaSB.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sativa = progress
                binding.newPlantSativaResultTV.text = "$sativa %"

                // Update dominance and hybrid status
                binding.newPlantSativaRB.isChecked = (sativa ?: 0) > (indica ?: 0)
                binding.newPlantHybridCB.isChecked = (indica ?: 0) > 0 && (sativa ?: 0) > 0
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Setup Indica SeekBar listener
        binding.newPlantGeneticIndicaSB.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                indica = progress
                binding.newPlantIndicaResultTV.text = "$indica %"

                // Update dominance and hybrid status
                binding.newPlantIndicaRB.isChecked = (indica ?: 0) > (sativa ?: 0)
                binding.newPlantHybridCB.isChecked = (indica ?: 0) > 0 && (sativa ?: 0) > 0
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Setup Ruderalis SeekBar listener
        binding.newPlantGeneticRuderalisSB.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ruderalis = progress
                binding.newPlantRuderalisResultTV.text = "$ruderalis %"

                // Update automatic status
                binding.newPlantAutomaticRB.isChecked = (ruderalis ?: 0) > 0
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Handle genetic type radio group changes
        binding.newPlantGeneticRG.setOnCheckedChangeListener { group, checkedId ->
            geneticType = when (checkedId) {
                R.id.newPlantRegularRB -> binding.newPlantRegularRB.text.toString()
                R.id.newPlantFeminizedRB -> binding.newPlantFeminizedRB.text.toString()
                R.id.newPlantAutomaticRB -> binding.newPlantAutomaticRB.text.toString()
                else -> "Unknown"
            }
        }

        // Expand and collapse views on card click
        binding.newPlantGeneticCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endDetail)
            expandDetail()
        }

        binding.newPlantCannabinoidsCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endCannabinoids)
            expandCannabinoids()
        }

        binding.newPlantFlowertimeCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endFlowertime)
            expandFlowertime()
        }

        // Collapse views on OK button click
        binding.newPlantGeneticOKBTN.setOnClickListener {
            binding.newPlantGeneticML.transitionToStart()
            collapseDetail()
            collapseCannabinoids()
            collapseFlowertime()
        }

        binding.newPlantFlowertimeOKBTN.setOnClickListener {
            binding.newPlantGeneticML.transitionToStart()
            collapseDetail()
            collapseCannabinoids()
            collapseFlowertime()
        }

        binding.newPlantCannabinoidsOKBTN.setOnClickListener {
            binding.newPlantGeneticML.transitionToStart()
            collapseDetail()
            collapseCannabinoids()
            collapseFlowertime()
        }

        // Save plant genetic data and navigate to next fragment
        binding.newPlantGeneticSaveBTN.setOnClickListener {
            var dominance: String? = null
            when (binding.newPlantGeneticDominanceRG.checkedRadioButtonId) {
                R.id.newPlantSativaRB -> dominance = "Sativa dominance"
                R.id.newPlantIndicaRB -> dominance = "Indica dominance"
            }

            // Update plant properties based on inputs
            plant.sativa = if ((sativa ?: 0) > 0) sativa.toString() else dominance
            plant.indica = if ((indica ?: 0) > 0) indica.toString() else dominance
            plant.breedingType = geneticType
            plant.cbd = if ((cbd ?: 0) > 0) cbd.toString() else null
            plant.thc = if ((thc ?: 0) > 0) thc.toString() else null
            plant.ruderalis = if ((ruderalis ?: 0) > 0) ruderalis.toString() else null
            plant.floweringTime = if ((flowertime ?: 0) > 0) flowertime else null

            // Save the updated plant data
            updatePlant(plant)

            // Navigate to the next fragment
            findNavController().navigate(
                NewPlantGeneticFragmentDirections.actionNewPlantGeneticFragmentToNewPlantStateFragment(
                    databaseID
                )
            )
        }
    }

    /** Update the plant data
     * @param plant the plant to be processed
     */
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

    /** Expand detail section with animation
     *
     */
    private fun expandDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(600)
            with(binding) {
                newPlantDetailBackgroundIV.visibility = View.GONE
                newPlantDetailTitleTV.visibility = View.GONE
                newPlantGeneticStrainTV.visibility = View.VISIBLE
                newPlantSativaTV.visibility = View.VISIBLE
                newPlantSativaResultTV.visibility = View.VISIBLE
                newPlantGeneticSativaSB.visibility = View.VISIBLE
                newPlantIndicaTV.visibility = View.VISIBLE
                newPlantIndicaResultTV.visibility = View.VISIBLE
                newPlantGeneticIndicaSB.visibility = View.VISIBLE
                newPlantRuderalisTV.visibility = View.VISIBLE
                newPlantRuderalisResultTV.visibility = View.VISIBLE
                newPlantGeneticRuderalisSB.visibility = View.VISIBLE
                newPlantHybridCB.visibility = View.VISIBLE
                newPlantGeneticDominanceRG.visibility = View.VISIBLE
            }
        }
    }

    /** Collapse detail section
     *
     */
    private fun collapseDetail() {
        with(binding) {
            newPlantDetailBackgroundIV.visibility = View.VISIBLE
            newPlantDetailTitleTV.visibility = View.VISIBLE
            newPlantGeneticStrainTV.visibility = View.INVISIBLE
            newPlantSativaTV.visibility = View.INVISIBLE
            newPlantSativaResultTV.visibility = View.INVISIBLE
            newPlantGeneticSativaSB.visibility = View.INVISIBLE
            newPlantIndicaTV.visibility = View.INVISIBLE
            newPlantIndicaResultTV.visibility = View.INVISIBLE
            newPlantGeneticIndicaSB.visibility = View.INVISIBLE
            newPlantRuderalisTV.visibility = View.INVISIBLE
            newPlantRuderalisResultTV.visibility = View.INVISIBLE
            newPlantGeneticRuderalisSB.visibility = View.INVISIBLE
            newPlantHybridCB.visibility = View.INVISIBLE
            newPlantGeneticDominanceRG.visibility = View.INVISIBLE
        }
    }

    /** Expand cannabinoids section with animation
     *
     */
    private fun expandCannabinoids() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(600)
            with(binding) {
                newPlantCannabinoidsBackgroundIV.visibility = View.GONE
                newPlantCannabinoidsTitleTV.visibility = View.GONE
                newPlantTHCTV.visibility = View.VISIBLE
                newPlantTHCSB.visibility = View.VISIBLE
                newPlantCBDTV.visibility = View.VISIBLE
                newPlantCBDSB.visibility = View.VISIBLE
            }
        }
    }

    /** Collapse cannabinoids section
     *
     */
    private fun collapseCannabinoids() {
        with(binding) {
            newPlantCannabinoidsBackgroundIV.visibility = View.VISIBLE
            newPlantCannabinoidsTitleTV.visibility = View.VISIBLE
            newPlantTHCTV.visibility = View.INVISIBLE
            newPlantTHCSB.visibility = View.INVISIBLE
            newPlantCBDTV.visibility = View.INVISIBLE
            newPlantCBDSB.visibility = View.INVISIBLE
        }
    }

    /** Expand flowertime section with animation
     *
     */
    private fun expandFlowertime() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(600)
            with(binding) {
                newPlantFlowertimeBackgroundIV.visibility = View.GONE
                newPlantFlowertimeTitleTV.visibility = View.GONE
                newPlantFlowertimeDescriptionTV.visibility = View.VISIBLE
                newPlantFlowertimeSB.visibility = View.VISIBLE
                newPlantFlowertimeDisplayTV.visibility = View.VISIBLE
            }
        }
    }

    /** Collapse flowertime section
     *
     */
    private fun collapseFlowertime() {
        with(binding) {
            newPlantFlowertimeBackgroundIV.visibility = View.VISIBLE
            newPlantFlowertimeTitleTV.visibility = View.VISIBLE
            newPlantFlowertimeDescriptionTV.visibility = View.INVISIBLE
            newPlantFlowertimeSB.visibility = View.INVISIBLE
            newPlantFlowertimeDisplayTV.visibility = View.INVISIBLE
        }
    }
}
