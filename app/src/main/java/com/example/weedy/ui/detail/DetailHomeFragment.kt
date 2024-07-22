package com.example.weedy.ui.detail

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentDetailHomeBinding
import com.example.weedy.ui.main.MainFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Fragment for displaying home details of a specific plant.
 */
class DetailHomeFragment : MainFragment() {

    private val TAG = "Detail Home Fragment" // Log tag for debugging
    private lateinit var binding: FragmentDetailHomeBinding // View binding for this fragment
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for data operations
    private val dateFormatter =
        DateTimeFormatter.ofPattern("dd.MM.yyyy") // Date formatter for displaying dates

    private lateinit var plant: MasterPlant // Current plant details

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the plant data and update UI accordingly
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant

            with(binding) {
                detailIndicaDisplayTV.text = plant.indica // Display indica information
                detailSativaDisplayTV.text = plant.sativa // Display sativa information
                detailRuderalisDisplayTV.text = plant.ruderalis // Display ruderalis information
                detailManufacturerTV.text = plant.seedCompany // Display seed company
                detailBreedingTypeTV.text = plant.breedingType // Display breeding type
            }

            // Fetch and display health and growth state information
            getHealthState(plant.id)
            getGrowthState(plant.id, plant.floweringTime)
        }
    }

    /**
     * Retrieves and displays the growth state of the plant.
     *
     * @param masterPlantID The ID of the plant.
     * @param flowertime The expected flowering time in weeks.
     */
    private fun getGrowthState(masterPlantID: Long, flowertime: Int?) {

        viewModel.getGrowthStateRecordsByPlantID(masterPlantID)
            .observe(viewLifecycleOwner) { growthStateRecords ->

                val growthState =
                    growthStateRecords.maxByOrNull { it.id } // Get the most recent growth state record

                // Update UI based on the current growth state
                when (growthState?.growthState) {
                    5 -> { // Flowering state
                        binding.detailProgressLatestUpdateTV.setText(R.string.flower)
                        binding.detailProgressStartDateTV.setText(
                            growthState.date.format(dateFormatter) // Format and display start date
                        )
                        binding.detailProgressEndDateTV.setText(flowertime?.let {
                            calcEndDate(
                                growthState.date,
                                it
                            ).format(dateFormatter) // Calculate and display end date
                        })
                        val week = calcWeekOf(growthState.date) // Calculate current week
                        binding.detailProgressWeekOfTV.text =
                            "Week $week of ${plant.floweringTime} weeks"

                        // Update progress bar
                        binding.detaillProgressWeekPB.apply {
                            max = flowertime ?: 0
                            progress = week
                        }
                    }

                    4 -> binding.detailProgressLatestUpdateTV.setText(R.string.vegetation)
                    3 -> binding.detailProgressLatestUpdateTV.setText(R.string.cutting)
                    2 -> binding.detailProgressLatestUpdateTV.setText(R.string.seedling)
                    1 -> binding.detailProgressLatestUpdateTV.setText(R.string.germination)
                    else -> binding.detailProgressLatestUpdateTV.setText(R.string.no_data)
                }
                Log.d(TAG, "$masterPlantID Growth State: ${growthState?.growthState}")
            }
    }

    /**
     * Retrieves and displays the health state of the plant.
     *
     * @param masterPlantID The ID of the plant.
     */
    private fun getHealthState(masterPlantID: Long) {

        viewModel.getHealthRecordByPlantID(masterPlantID)
            .observe(viewLifecycleOwner) { healthRecords ->

                val health =
                    healthRecords.maxByOrNull { it.id } // Get the most recent health record

                // Update health status image based on the current health level
                when (health?.health) {
                    100 -> binding.detailHealthIV.setImageResource(R.drawable.health_100)
                    80 -> binding.detailHealthIV.setImageResource(R.drawable.health_80)
                    60 -> binding.detailHealthIV.setImageResource(R.drawable.health_60)
                    40 -> binding.detailHealthIV.setImageResource(R.drawable.health_40)
                    20 -> binding.detailHealthIV.setImageResource(R.drawable.health_20)
                    0 -> binding.detailHealthIV.setImageResource(R.drawable.health_0)
                    else -> binding.detailHealthIV.setImageResource(R.drawable.no_data)
                }
                Log.d(TAG, "$masterPlantID Health: ${health?.health}")
            }
    }

    /**
     * Calculates the end date of flowering based on the start date and flowering time.
     *
     * @param startDate The start date of the flowering period.
     * @param flowertime The expected flowering time in weeks.
     * @return The calculated end date of the flowering period.
     */
    private fun calcEndDate(startDate: LocalDate, flowertime: Int): LocalDate {
        return startDate.plusDays(flowertime * 7L) // Add flowering time in days to the start date
    }

    /**
     * Calculates the number of weeks since the start date.
     *
     * @param startDate The start date to compare against the current date.
     * @return The number of weeks between the start date and the current date.
     */
    private fun calcWeekOf(startDate: LocalDate): Int {
        return ChronoUnit.WEEKS.between(startDate, LocalDate.now())
            .toInt() // Calculate the number of weeks
    }

    /**
     * Called when an image is captured.
     *
     * @param imageBitmap The captured image as a Bitmap.
     */
    override fun onImageCaptured(imageBitmap: Bitmap) {
        // TODO: Implement this method if needed
    }

    /**
     * Called when an image is picked from the gallery.
     *
     * @param imageUri The URI of the picked image.
     */
    override fun onImagePicked(imageUri: Uri?) {
        // TODO: Implement this method if needed
    }
}
