package com.example.weedy.ui.detail

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.adapter.list.ListWaterAdapter
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentDetailWaterBinding
import com.example.weedy.ui.main.MainFragment

/**
 * Fragment to display details about plant watering records.
 */
class DetailWaterFragment : MainFragment() {

    private val TAG = "Detail Water Fragment" // Tag for logging purposes
    private lateinit var binding: FragmentDetailWaterBinding // Binding for the fragment's layout
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for plant data

    private lateinit var plant: MasterPlant // Current plant details
    private lateinit var waterRecyclerView: RecyclerView // RecyclerView for displaying watering records

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailWaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView after setting up the binding
        waterRecyclerView = binding.detailWaterRV

        // Observe the plant data to update the UI
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant

            // Fetch and display watering records for the current plant
            viewModel.getWateringRecordByPlantID(plant.id)
                .observe(viewLifecycleOwner) { wateringRecords ->
                    Log.d(
                        TAG,
                        "Watering Record size: ${wateringRecords.size}"
                    ) // Log the size of watering records
                    waterRecyclerView.adapter =
                        ListWaterAdapter(wateringRecords) // Set the adapter for RecyclerView
                }
        }
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
