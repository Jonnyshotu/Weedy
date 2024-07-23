package com.example.weedy.ui.new_plant

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentNewPlantGeneticBinding
import com.example.weedy.ui.main.MainFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class NewPlantGeneticFragment : MainFragment() {

    private val TAG = "New Plant Genetic Fragment" // Tag for logging
    private lateinit var binding: FragmentNewPlantGeneticBinding // Binding object for the fragment's layout
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for data

    private val args: NewPlantGeneticFragmentArgs by navArgs() // Arguments passed via navigation
    private lateinit var plant: MasterPlant // Master plant object to be updated

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentNewPlantGeneticBinding.inflate(inflater, container, false) // Inflate the layout
        return binding.root // Return the root view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedCloseML()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // Get the plant ID passed via navigation
        val databaseID = args.databaseMasterPlantID // Plant ID from navigation
        Log.d(TAG, "Navigation plant ID: $databaseID")

        // Retrieve the plant data by ID
        viewModel.getPlantByID(databaseID) // Request plant data from ViewModel

        // Observe the plant data
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant // Update local plant object
            binding.newPlantGeneticStrainTV.text = plant.strainName // Set strain name
        }

        var thc: Float? = null // THC value
        var cbd: Float? = null // CBD value
        var flowertime: Float? = null // Flowering time
        var sativa: Float? = null // Sativa percentage
        var indica: Float? = null // Indica percentage
        var ruderalis: Float? = null // Ruderalis percentage
        var geneticType: String? = null // Genetic type

        // Setup THC SeekBar listener
        binding.newPlantTHCSB.addOnChangeListener { _, thcInput, _ ->
            thc = thcInput // Update THC value
            binding.newPlantTHCResultTV.text = "%.0f%%".format(thc) // Display THC result
        }

        // Setup CBD SeekBar listener
        binding.newPlantCBDSB.addOnChangeListener { _, cbdInput, _ ->
            cbd = cbdInput // Update CBD value
            binding.newPlantCBDResultTV.text = "%.0f%%".format(cbd) // Display CBD result
        }

        // Setup Flowertime SeekBar listener
        binding.newPlantFlowertimeSB.addOnChangeListener { _, flowerTimeInput, _ ->
            flowertime = flowerTimeInput // Update flowering time
            binding.newPlantFlowertimeDisplayTV.text =
                "%.0f Weeks".format(flowertime) // Display flowering time
        }

        // Setup Sativa SeekBar listener
        binding.newPlantGeneticSativaSB.addOnChangeListener { _, sativaInput, _ ->
            sativa = sativaInput // Update Sativa percentage
            setSliderMaxValues(sativa, indica, ruderalis) // Update slider max values
            setInputOnRadioGroup(sativa, indica, ruderalis) // Update radio group based on input
            binding.newPlantSativaResultTV.text = "%.0f%%".format(sativa) // Display Sativa result
        }

        // Setup Indica SeekBar listener
        binding.newPlantGeneticIndicaSB.addOnChangeListener { _, indicaInput, _ ->
            indica = indicaInput // Update Indica percentage
            setSliderMaxValues(sativa, indica, ruderalis) // Update slider max values
            setInputOnRadioGroup(sativa, indica, ruderalis) // Update radio group based on input
            binding.newPlantIndicaResultTV.text = "%.0f%%".format(indica) // Display Indica result
        }

        // Setup Ruderalis SeekBar listener
        binding.newPlantGeneticRuderalisSB.addOnChangeListener { _, ruderalisInput, _ ->
            ruderalis = ruderalisInput // Update Ruderalis percentage
            setSliderMaxValues(sativa, indica, ruderalis) // Update slider max values
            setInputOnRadioGroup(sativa, indica, ruderalis) // Update radio group based on input
            binding.newPlantRuderalisResultTV.text =
                "%.0f%%".format(ruderalis) // Display Ruderalis result
        }

        // Handle genetic type radio group changes
        binding.newPlantGeneticRG.setOnCheckedChangeListener { group, checkedId ->
            geneticType = when (checkedId) {
                R.id.newPlantRegularRB -> binding.newPlantRegularRB.text.toString() // Regular
                R.id.newPlantFeminizedRB -> binding.newPlantFeminizedRB.text.toString() // Feminized
                R.id.newPlantAutomaticRB -> binding.newPlantAutomaticRB.text.toString() // Automatic
                else -> "Unknown" // Default value
            }
        }

        // Expand and collapse views on card click
        binding.newPlantGeneticCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endDetail) // Transition to detail state
            expandDetail() // Expand detail section
        }

        binding.newPlantCannabinoidsCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endCannabinoids) // Transition to cannabinoids state
            expandCannabinoids() // Expand cannabinoids section
        }

        binding.newPlantFlowertimeCV.setOnClickListener {
            binding.newPlantGeneticML.transitionToState(R.id.endFlowertime) // Transition to flowertime state
            expandFlowertime() // Expand flowertime section
        }

        // Collapse views on OK button click
        binding.newPlantGeneticOKBTN.setOnClickListener {
            binding.newPlantGeneticML.transitionToStart() // Transition to start state
            collapseDetail() // Collapse detail section
            collapseCannabinoids() // Collapse cannabinoids section
            collapseFlowertime() // Collapse flowertime section
        }

        binding.newPlantFlowertimeOKBTN.setOnClickListener {
            binding.newPlantGeneticML.transitionToStart() // Transition to start state
            collapseDetail() // Collapse detail section
            collapseCannabinoids() // Collapse cannabinoids section
            collapseFlowertime() // Collapse flowertime section
        }

        binding.newPlantCannabinoidsOKBTN.setOnClickListener {
            binding.newPlantGeneticML.transitionToStart() // Transition to start state
            collapseDetail() // Collapse detail section
            collapseCannabinoids() // Collapse cannabinoids section
            collapseFlowertime() // Collapse flowertime section
        }

        // Save plant genetic data and navigate to next fragment
        binding.newPlantGeneticSaveBTN.setOnClickListener {
            var dominance: String? = null // Dominance type
            when (binding.newPlantGeneticDominanceRG.checkedRadioButtonId) {
                R.id.newPlantSativaRB -> dominance = "Sativa dominance" // Sativa dominance
                R.id.newPlantIndicaRB -> dominance = "Indica dominance" // Indica dominance
            }

            // Update plant properties based on inputs
            plant.sativa = if ((sativa?.toInt() ?: 0) > 0) sativa.toString() else dominance
            plant.indica = if ((indica?.toInt() ?: 0) > 0) indica.toString() else dominance
            plant.breedingType = geneticType
            plant.cbd = if ((cbd?.toInt() ?: 0) > 0) cbd.toString() else null
            plant.thc = if ((thc?.toInt() ?: 0) > 0) thc.toString() else null
            plant.ruderalis = if ((ruderalis?.toInt() ?: 0) > 0) ruderalis.toString() else null
            plant.floweringTime = if ((flowertime?.toInt() ?: 0) > 0) flowertime?.toInt() else null

            // Save the updated plant data
            updatePlant(plant) // Call update function

            // Navigate to the next fragment
            findNavController().navigate(
                NewPlantGeneticFragmentDirections.actionNewPlantGeneticFragmentToNewPlantStateFragment(
                    databaseID
                )
            )
        }
    }

    private fun onBackPressedCloseML(){
        collapseDetail()
        collapseFlowertime()
        collapseCannabinoids()
        binding.newPlantGeneticML.transitionToStart()
    }

    /**
     * Update the plant data
     * @param plant the plant to be processed
     */
    private fun updatePlant(plant: MasterPlant) {
        try {
            viewModel.updatePlant(plant) // Update plant in ViewModel
            Toast.makeText(context, "Plant updated", Toast.LENGTH_SHORT)
                .show() // Show success message
        } catch (e: Exception) {
            Log.e(TAG, "Error updating plant", e) // Log error
            Toast.makeText(context, "An error occurred while updating plant", Toast.LENGTH_LONG)
                .show() // Show error message
        }
    }

    /**
     * Set input values on the radio group based on slider inputs
     * @param sativa Sativa percentage
     * @param indica Indica percentage
     * @param ruderalis Ruderalis percentage
     */
    private fun setInputOnRadioGroup(sativa: Float?, indica: Float?, ruderalis: Float?) {
        if ((sativa ?: 0f) > (indica ?: 0f)) binding.newPlantSativaRB.isChecked = true
        else if ((sativa ?: 0f) < (indica ?: 0f)) binding.newPlantIndicaRB.isChecked = true
        else {
            binding.newPlantSativaRB.isChecked = false
            binding.newPlantSativaRB.isChecked = false
        }

        if ((sativa ?: 0f) > 0 && (indica ?: 0f) > 0) binding.newPlantHybridCB.isChecked =
            true else binding.newPlantHybridCB.isChecked = false

        if ((ruderalis ?: 0f) > 0f) binding.newPlantAutomaticRB.isChecked =
            true else binding.newPlantAutomaticRB.isChecked = false
    }

    /**
     * Set maximum values for sliders based on current inputs
     * @param sativa Sativa percentage
     * @param indica Indica percentage
     * @param ruderalis Ruderalis percentage
     */
    fun setSliderMaxValues(sativa: Float?, indica: Float?, ruderalis: Float?) {
        val totalLimit = 100f // Total limit for sliders
        val stepSize = 1f // Step size for slider values
        val minValue = 0f // Minimum slider value

        fun getValidValueTo(maxValue: Float): Float {
            return max(
                (maxValue / stepSize).roundToInt() * stepSize,
                minValue + stepSize
            ) // Calculate valid max value
        }

        val sativaValue = sativa ?: 0f
        val indicaValue = indica ?: 0f
        val ruderalisValue = ruderalis ?: 0f

        val usedTotal = sativaValue + indicaValue + ruderalisValue
        val remaining = max(totalLimit - usedTotal, 0f)

        // Update max values for the Sativa and Indica sliders
        binding.newPlantGeneticSativaSB.valueTo =
            getValidValueTo(min(sativaValue + remaining, totalLimit))
        binding.newPlantGeneticIndicaSB.valueTo =
            getValidValueTo(min(indicaValue + remaining, totalLimit))
        binding.newPlantGeneticRuderalisSB.valueTo =
            getValidValueTo(min(ruderalisValue + remaining, totalLimit))

        // Hide the Ruderalis slider if Sativa and Indica sum up to 100
        if (usedTotal == totalLimit && binding.newPlantGeneticRuderalisSB.value == 0f) {
            binding.newPlantGeneticRuderalisSB.visibility = View.INVISIBLE
            binding.newPlantRuderalisTV.visibility = View.INVISIBLE
            binding.newPlantRuderalisResultTV.visibility = View.INVISIBLE
        } else {
            binding.newPlantGeneticRuderalisSB.visibility = View.VISIBLE
            binding.newPlantRuderalisTV.visibility = View.VISIBLE
            binding.newPlantRuderalisResultTV.visibility = View.VISIBLE
        }

        // Ensure current values are within the new max values and greater than minValue
        binding.newPlantGeneticSativaSB.value = max(
            min(binding.newPlantGeneticSativaSB.value, binding.newPlantGeneticSativaSB.valueTo),
            minValue
        )
        binding.newPlantGeneticIndicaSB.value = max(
            min(binding.newPlantGeneticIndicaSB.value, binding.newPlantGeneticIndicaSB.valueTo),
            minValue
        )
        binding.newPlantGeneticRuderalisSB.value = max(
            min(
                binding.newPlantGeneticRuderalisSB.value,
                binding.newPlantGeneticRuderalisSB.valueTo
            ), minValue
        )
    }

    /**
     * Expand detail section with animation
     *
     */
    private fun expandDetail() {
        with(binding) {
            animateViewToGone(newPlantDetailBackgroundIV) // Hide background
            animateViewToGone(newPlantDetailTitleTV) // Hide title

            animateViewToVisible(newPlantGeneticStrainTV) // Show strain name
            animateViewToVisible(newPlantSativaTV) // Show Sativa label
            animateViewToVisible(newPlantSativaResultTV) // Show Sativa result
            animateViewToVisible(newPlantGeneticSativaSB) // Show Sativa slider
            animateViewToVisible(newPlantIndicaTV) // Show Indica label
            animateViewToVisible(newPlantIndicaResultTV) // Show Indica result
            animateViewToVisible(newPlantGeneticIndicaSB) // Show Indica slider
            animateViewToVisible(newPlantRuderalisTV) // Show Ruderalis label
            animateViewToVisible(newPlantRuderalisResultTV) // Show Ruderalis result
            animateViewToVisible(newPlantGeneticRuderalisSB) // Show Ruderalis slider
            animateViewToVisible(newPlantHybridCB) // Show Hybrid checkbox
            animateViewToVisible(newPlantGeneticDominanceRG) // Show Dominance radio group
            animateViewToVisible(newPlantGeneticOKBTN) // Show OK button
        }
    }

    /**
     * Collapse detail section
     *
     */
    private fun collapseDetail() {
        with(binding) {
            animateViewToVisible(newPlantDetailBackgroundIV) // Show background
            animateViewToVisible(newPlantDetailTitleTV) // Show title

            animateViewToInvisible(newPlantGeneticStrainTV) // Hide strain name
            animateViewToInvisible(newPlantSativaTV) // Hide Sativa label
            animateViewToInvisible(newPlantSativaResultTV) // Hide Sativa result
            animateViewToInvisible(newPlantGeneticSativaSB) // Hide Sativa slider
            animateViewToInvisible(newPlantIndicaTV) // Hide Indica label
            animateViewToInvisible(newPlantIndicaResultTV) // Hide Indica result
            animateViewToInvisible(newPlantGeneticIndicaSB) // Hide Indica slider
            animateViewToInvisible(newPlantRuderalisTV) // Hide Ruderalis label
            animateViewToInvisible(newPlantRuderalisResultTV) // Hide Ruderalis result
            animateViewToInvisible(newPlantGeneticRuderalisSB) // Hide Ruderalis slider
            animateViewToInvisible(newPlantHybridCB) // Hide Hybrid checkbox
            animateViewToInvisible(newPlantGeneticDominanceRG) // Hide Dominance radio group
            animateViewToInvisible(newPlantGeneticOKBTN) // Hide OK button
        }
    }

    /**
     * Expand cannabinoids section with animation
     *
     */
    private fun expandCannabinoids() {
        with(binding) {
            animateViewToGone(newPlantCannabinoidsBackgroundIV) // Hide background
            animateViewToGone(newPlantCannabinoidsTitleTV) // Hide title

            animateViewToVisible(newPlantTHCTV) // Show THC label
            animateViewToVisible(newPlantTHCResultTV) // Show THC result
            animateViewToVisible(newPlantTHCSB) // Show THC slider
            animateViewToVisible(newPlantCBDTV) // Show CBD label
            animateViewToVisible(newPlantCBDResultTV) // Show CBD result
            animateViewToVisible(newPlantCBDSB) // Show CBD slider
            animateViewToVisible(newPlantCannabinoidsOKBTN) // Show OK button
        }
    }

    /**
     * Collapse cannabinoids section
     *
     */
    private fun collapseCannabinoids() {
        with(binding) {
            animateViewToVisible(newPlantCannabinoidsBackgroundIV) // Show background
            animateViewToVisible(newPlantCannabinoidsTitleTV) // Show title

            animateViewToInvisible(newPlantTHCTV) // Hide THC label
            animateViewToInvisible(newPlantTHCResultTV) // Hide THC result
            animateViewToInvisible(newPlantTHCSB) // Hide THC slider
            animateViewToInvisible(newPlantCBDTV) // Hide CBD label
            animateViewToInvisible(newPlantCBDResultTV) // Hide CBD result
            animateViewToInvisible(newPlantCBDSB) // Hide CBD slider
            animateViewToInvisible(newPlantCannabinoidsOKBTN) // Hide OK button
        }
    }

    /**
     * Expand flowertime section with animation
     *
     */
    private fun expandFlowertime() {
        with(binding) {
            animateViewToGone(newPlantFlowertimeBackgroundIV) // Hide background
            animateViewToGone(newPlantFlowertimeTitleTV) // Hide title

            animateViewToVisible(newPlantFlowertimeDescriptionTV) // Show flowering time description
            animateViewToVisible(newPlantFlowertimeOKBTN) // Show OK button
            animateViewToVisible(newPlantFlowertimeSB) // Show flowering time slider
            animateViewToVisible(newPlantFlowertimeDisplayTV) // Show flowering time display
        }
    }

    /**
     * Collapse flowertime section
     *
     */
    private fun collapseFlowertime() {
        with(binding) {
            animateViewToVisible(newPlantFlowertimeBackgroundIV) // Show background
            animateViewToVisible(newPlantFlowertimeTitleTV) // Show title

            animateViewToInvisible(newPlantFlowertimeDescriptionTV) // Hide flowering time description
            animateViewToInvisible(newPlantFlowertimeSB) // Hide flowering time slider
            animateViewToInvisible(newPlantFlowertimeDisplayTV) // Hide flowering time display
            animateViewToInvisible(newPlantFlowertimeOKBTN) // Hide OK button
        }
    }

    override fun onImageCaptured(imageBitmap: Bitmap) {
        // Handle image capture if needed
    }

    override fun onImagePicked(imageUri: Uri?) {
        // Handle image pick if needed
    }
}
