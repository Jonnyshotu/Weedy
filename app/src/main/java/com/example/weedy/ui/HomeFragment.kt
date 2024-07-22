package com.example.weedy.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.SharedViewModel
import com.example.weedy.adapter.OnClick
import com.example.weedy.adapter.PlantAdapter
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.databinding.FragmentHomeBinding
import com.example.weedy.ui.main.MainFragment

/**
 * Fragment for displaying the home screen with a list of plants.
 */
class HomeFragment : MainFragment(), OnClick {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PlantAdapter // Adapter for displaying plant items
    private lateinit var recyclerView: RecyclerView // RecyclerView for displaying plant items
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for managing UI-related data

    private val TAG = "Home Fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        // Initialize RecyclerView and Adapter
        recyclerView = binding.homeRV
        adapter = PlantAdapter(this)
        recyclerView.adapter = adapter

        // Check and request permissions if not granted
        if (!allPermissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe plant list changes and update UI accordingly
        viewModel.plantList.observe(viewLifecycleOwner) { masterPlantList ->
            Log.d(TAG, "Master plant list size: ${masterPlantList.size}")
            viewModel.createDisplayPlants(masterPlantList) // Process and update display plants
        }

        // Observe health records updates
        viewModel.healthRecordsList.observe(viewLifecycleOwner) {
            Log.d(TAG, "Health records updated")
            viewModel.updateHealthStatus() // Update health status
        }

        // Observe growth records updates
        viewModel.growthRecordsList.observe(viewLifecycleOwner) {
            Log.d(TAG, "Growth records updated")
            viewModel.updateAge() // Update plant age based on growth records
        }

        // Observe growth records updates
        viewModel.imageRecordsList.observe(viewLifecycleOwner) {
            Log.d(TAG, "Image records updated")
            viewModel.updateImage() // Update plant image based on image records
        }

        // Observe display plant list and update adapter
        viewModel.displayPlantList.observe(viewLifecycleOwner) { displayPlantList ->
            Log.d(TAG, "Display plants updated")
            adapter.submitList(displayPlantList.sortedBy { displayPlant -> displayPlant.strainName })
        }



        // Handle Add Plant button click
        binding.homeAddFAB.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewPlantFragment())
        }
    }

    /**
     * Handles click events on a plant item to navigate to the detail view.
     * @param masterPlantID The ID of the plant that was clicked.
     */
    override fun onPlantClick(masterPlantID: Long) {
        Log.d(TAG, "Plant id: $masterPlantID")
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(masterPlantID)
        )
    }

    /**
     * Handles click events on a genetic strain item. Currently not implemented.
     * @param plant The LocalGenetic item that was clicked.
     * @param view The view that was clicked.
     */
    override fun onGeneticClick(plant: LocalGenetic, view: View) {}

    /**
     * Handles click events on a treatment item to show the treatment menu.
     * @param masterPlantID The ID of the plant for which to show the treatment menu.
     * @param view The view that was clicked.
     */
    override fun onTreatmentClick(masterPlantID: Long, view: View) {
        showTreatmentMenu(masterPlantID, view) // Show treatment options menu
    }

    /**
     * Handles image capture events. Currently not implemented.
     * @param imageBitmap The captured image as a Bitmap.
     */
    override fun onImageCaptured(imageBitmap: Bitmap) {}

    /**
     * Handles image pick events. Currently not implemented.
     * @param imageUri The URI of the picked image.
     */
    override fun onImagePicked(imageUri: Uri?) {}
}
