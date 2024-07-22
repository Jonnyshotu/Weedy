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
import com.example.weedy.adapter.list.ListTrainingAdapter
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentDetailTrainingBinding
import com.example.weedy.ui.main.MainFragment

/**
 * Fragment to display details about plant training records.
 */
class DetailTrainingFragment : MainFragment() {

    private val TAG = "Detail Training Fragment" // Log tag for debugging
    private lateinit var binding: FragmentDetailTrainingBinding // View binding for this fragment
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for managing plant data

    private lateinit var plant: MasterPlant // Current plant details
    private lateinit var trainingRecyclerView: RecyclerView // RecyclerView for displaying training records

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView after setting up binding
        trainingRecyclerView = binding.detailTrainingRV
        val adapter = ListTrainingAdapter()
        trainingRecyclerView.adapter = adapter

        // Observe the plant data to update the UI
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant

            // Fetch and display training records for the current plant
            viewModel.getTrainingRecordByPlantID(plant.id)
                .observe(viewLifecycleOwner) { trainingRecords ->
                    Log.d(
                        TAG,
                        "Training Record size: ${trainingRecords.size}"
                    ) // Log the size of training records
                    adapter.submitList(trainingRecords) // Update the adapter for RecyclerView
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
