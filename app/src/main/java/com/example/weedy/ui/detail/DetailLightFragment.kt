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
import com.example.weedy.adapter.list.ListLightAdapter
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentDetailLightBinding
import com.example.weedy.ui.main.MainFragment

/**
 * Fragment for displaying light-related details of a specific plant.
 */
class DetailLightFragment : MainFragment() {

    private val TAG = "Detail Light Fragment" // Log tag for debugging
    private lateinit var binding: FragmentDetailLightBinding // View binding for this fragment
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for data operations

    private lateinit var plant: MasterPlant // Current plant details
    private lateinit var lightRecyclerView: RecyclerView // RecyclerView for displaying light records

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailLightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView after setting up binding
        lightRecyclerView = binding.detailLightRV

        // Observe the plant data and update UI accordingly
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant

            // Fetch and display light records for the current plant
            viewModel.getLightRecordsByPlantID(plant.id)
                .observe(viewLifecycleOwner) { lightRecords ->
                    Log.d(
                        TAG,
                        "Light Record size: ${lightRecords.size}"
                    ) // Log the size of light records
                    lightRecyclerView.adapter =
                        ListLightAdapter(lightRecords) // Set the adapter for RecyclerView
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
