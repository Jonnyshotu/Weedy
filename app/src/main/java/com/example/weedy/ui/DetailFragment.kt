package com.example.weedy.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.SharedViewModel
import com.example.weedy.adapter.list.ListMeasurementsAdapter
import com.example.weedy.adapter.list.ListNutrientsAdapter
import com.example.weedy.adapter.list.ListRepellentsAdapter
import com.example.weedy.adapter.list.ListTrainingAdapter
import com.example.weedy.adapter.list.ListWaterAdapter
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentDetailBinding
import com.example.weedy.ui.main.MainFragment

class DetailFragment : MainFragment() {

    private val TAG = "Detail Fragment"

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var plant: MasterPlant

    //region Recycler views

    private lateinit var waterAdapter: ListWaterAdapter
    private lateinit var nutrientsAdapter: ListNutrientsAdapter
    private lateinit var repellentsAdapter: ListRepellentsAdapter
    private lateinit var trainingAdapter: ListTrainingAdapter
    private lateinit var measurementsAdapter: ListMeasurementsAdapter
    private lateinit var waterRecyclerView: RecyclerView
    private lateinit var nutrientsRecyclerView: RecyclerView
    private lateinit var repellentsRecyclerView: RecyclerView
    private lateinit var trainingRecyclerView: RecyclerView
    private lateinit var measurementsRecyclerView: RecyclerView

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the plant ID passed via navigation
        val databaseID = args.masterPlantDatabaseID
        Log.d(TAG, "Navigation plant ID: $databaseID")

        // Retrieve the plant data by ID
        viewModel.getPlantByID(databaseID)

        // Observe the plant data
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant

            with(binding) {

                detailToolbar.title = plant.strainName
                detailIndicaDisplayTV.text = plant.indica
                detailSativaDisplayTV.text = plant.sativa
                detailRuderalisDisplayTV.text = plant.ruderalis
                detailManufacturerTV.text = plant.seedCompany
                detailGerminationTV.text = plant.breedingType

                waterRecyclerView = detailWaterRV

                viewModel.getWateringRecordByPlantID(plant.id).observe(viewLifecycleOwner) { wateringRecords ->
                    Log.d(TAG, "Watering Recordsize: ${wateringRecords.size}")
                    waterRecyclerView.adapter = ListWaterAdapter(wateringRecords)
                }
            }
        }

        with(binding) {

//            detailProgressStartDateTV.text = plant.germinationWaterActions.find { it. }
//            val weeksTilHarvest = plant.harvestDate
//            val weeksOld = plant.weeksOld
//            detailProgressEndDateTV.text = weeksTilHarvest.toString()
//            detailProgressWeekOfTV.text = "Week $weeksOld of ${plant.genetic.floweringTime}"
//            with(detaillProgressWeekPB) {
//                max = plant.genetic.floweringTime
//                progress = weeksOld.toInt()
//            }

//            detailProgressStartGerminationWaterTV.text =
//                "Germination Water: ${plant.germinationWater?.germinationWater}"
//            detailProgressSoilTV.text =
//                "Germination Soil: ${plant.germinationSoil?.germinationSoil}"
//
//            val latestEntry = plant.light?.lightHours
//            if (latestEntry != null) {
//                detaillLightCyclePB.progress = latestEntry
//                detaillLightHoursTV.text =
//                    "${latestEntry} hours light on and ${24 - latestEntry} hours off"
//            } else {
//                detaillLightCyclePB.progress = 0
//                detaillLightHoursTV.text = "No data"
//            }

            //region Recycler Views
//
//            nutrientsRecyclerView = detailNutrientsRV
//
//            if (plant.nutrients.isNotEmpty()) {
//                nutrientsAdapter = ListNutrientsAdapter(plant.nutrients)
//                nutrientsRecyclerView.adapter = nutrientsAdapter
//            } else {
//                detailNutrientsCV.visibility = View.GONE
//                detailNutrientsTV.text = "No nutrients records"
//            }
//
//            repellentsRecyclerView = detailRepellentsRV
//
//            if (plant.repellents.isNotEmpty()) {
//                repellentsAdapter = ListRepellentsAdapter(plant.repellents)
//                repellentsRecyclerView.adapter = repellentsAdapter
//            } else {
//                detailRepellentsCV.visibility = View.GONE
//                detailRepellentsTV.text = "No repellents records"
//            }
//
//            trainingRecyclerView = detailTrainingRV
//
//            if (plant.training.isNotEmpty()) {
//                trainingAdapter = ListTrainingAdapter(plant.training)
//                trainingRecyclerView.adapter = trainingAdapter
//            } else {
//                detailTrainingCV.visibility = View.GONE
//                detailTrainingTV.text = "No training records"
//            }
//
//            measurementsRecyclerView = detailMeasurementsRV
//
//            if (plant.measurements.isNotEmpty()) {
//                measurementsAdapter = ListMeasurementsAdapter(plant.measurements)
//                measurementsRecyclerView.adapter = measurementsAdapter
//            } else {
//                detailMeasurementsCV.visibility = View.GONE
//                detailMeasurementsTV.text = "No measurement records"
//            }
//

            //endregion
        }
    }

    override fun onImageCaptured(imageBitmap: Bitmap) {
        TODO("Not yet implemented")
    }

    override fun onImagePicked(imageUri: Uri?) {
        TODO("Not yet implemented")
    }
}