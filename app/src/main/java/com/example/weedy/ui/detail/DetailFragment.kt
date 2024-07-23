package com.example.weedy.ui.detail

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weedy.SharedViewModel
import com.example.weedy.adapter.list.ListMeasurementsAdapter
import com.example.weedy.adapter.list.ListNutrientsAdapter
import com.example.weedy.adapter.list.ListRepellentsAdapter
import com.example.weedy.adapter.list.ListTrainingAdapter
import com.example.weedy.adapter.list.ListWaterAdapter
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentDetailBinding
import com.example.weedy.ui.main.MainFragment
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : MainFragment() {

    private val TAG = "Detail Fragment"

    private lateinit var binding: FragmentDetailBinding // View binding for this fragment
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for data operations
    private val args: DetailFragmentArgs by navArgs() // Arguments passed to this fragment
    private lateinit var plant: MasterPlant // Current plant details

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 7 // Total number of pages

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DetailHomeFragment() // Home tab
                1 -> DetailWaterFragment() // Watering tab
                2 -> DetailNutrientsFragment() // Nutrients tab
                3 -> DetailRepellentsFragment() // Repellents tab
                4 -> DetailTrainingFragment() // Training tab
                5 -> DetailLightFragment() // Light tab
                6 -> DetailRepotFragment() // Repot tab
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager() // Initialize the ViewPager with fragments
        setupPlantData() // Load plant data and update UI
        setupActionBTN() // Set up the action button

        // Set up toolbar navigation
        binding.detailToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    /**
     * Sets up the ViewPager with tabs using TabLayoutMediator.
     */
    private fun setupViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        binding.detailVP.adapter = viewPagerAdapter

        TabLayoutMediator(binding.detailTL, binding.detailVP) { tab, position ->
            tab.text = when (position) {
                0 -> "Details"
                1 -> "Watering"
                2 -> "Nutrients"
                3 -> "Repellents"
                4 -> "Training"
                5 -> "Light"
                6 -> "Repot"
                else -> null
            }
        }.attach()

        binding.detailVP.offscreenPageLimit = 7  // Loads all fragments to avoid layout crash if scrolled to fast
    }

    /**
     * Loads plant data from the ViewModel and updates the UI.
     */
    private fun setupPlantData() {
        val databaseID = args.masterPlantDatabaseID // Plant ID from arguments
        Log.d(TAG, "Navigation plant ID: $databaseID")

        viewModel.getPlantByID(databaseID) // Fetch plant data by ID

        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant
            updateUI() // Update UI with the fetched plant data
        }
    }

    /**
     * Updates the UI elements with plant details.
     */
    private fun updateUI() {
        binding.detailTitleTV.text = plant.strainName // Set plant name in title
        Log.d(TAG, "update ui: ${plant.strainName}")
    }

    /**
     * Sets up the action button to show a treatment menu.
     */
    private fun setupActionBTN() {
        binding.detailActionBTN.setOnClickListener {
            showTreatmentMenu(plant.id, it) // Show treatment options for the plant
        }
    }

    /**
     * Called when an image is captured.
     *
     * @param imageBitmap The captured image as a Bitmap.
     */
    override fun onImageCaptured(imageBitmap: Bitmap) {
        TODO("Not yet implemented")
    }

    /**
     * Called when an image is picked from the gallery.
     *
     * @param imageUri The URI of the picked image.
     */
    override fun onImagePicked(imageUri: Uri?) {
        TODO("Not yet implemented")
    }
}
