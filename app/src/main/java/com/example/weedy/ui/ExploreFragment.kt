package com.example.weedy.ui

import com.example.weedy.adapter.ExploreAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.ExploreViewModel
import com.example.weedy.R
import com.example.weedy.adapter.OnClick
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.databinding.FragmentExploreBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment for exploring and filtering genetic strains.
 */
class ExploreFragment : Fragment(), OnClick {

    private val TAG = "Explore Fragment"

    private lateinit var binding: FragmentExploreBinding
    private lateinit var exploreRV: RecyclerView // RecyclerView for displaying genetic strains
    private lateinit var adapter: ExploreAdapter // Adapter for the RecyclerView

    private val viewModel: ExploreViewModel by activityViewModels() // ViewModel for managing UI-related data

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupResetBTN()
        setupSearchInput()
        setupGeneticTypeRadioGroup()
        setupTHCSlider()
        setupScrollListener()
        setupMotionLayoutListener()

        // Observe filtered genetic list and update adapter
        viewModel.filteredGeneticList.observe(viewLifecycleOwner) { filteredList ->
            adapter.submitList(filteredList)
        }
    }

    /**
     * Sets up the RecyclerView with an adapter.
     */
    private fun setupRecyclerView() {
        exploreRV = binding.exploreRV
        adapter = ExploreAdapter(this) // Set the adapter for RecyclerView
        exploreRV.adapter = adapter
    }

    /**
     * Sets up the reset button to clear filters and reset the ViewModel.
     */
    private fun setupResetBTN() {
        binding.exploreResetBTN.setOnClickListener {
            // Clear search input, radio group, and slider
            binding.exploreSearchET.text = null
            binding.exploreRG.clearCheck()
            binding.exploreTHCSlider.value = 0F
            viewModel.resetFilter() // Reset filters in ViewModel
        }
    }

    /**
     * Sets up a text watcher for the search input field.
     */
    private fun setupSearchInput() {
        binding.exploreSearchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            /**
             * Called after the text in the search input field has been changed.
             * @param s The new text in the search input field.
             */
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateSearchInput(s?.toString()) // Update search input in ViewModel
            }
        })
    }

    /**
     * Sets up the radio group for selecting genetic type.
     */
    private fun setupGeneticTypeRadioGroup() {
        binding.exploreRG.setOnCheckedChangeListener { group, checkedId ->
            val geneticType = when (checkedId) {
                R.id.exploreHybridRB -> binding.exploreHybridRB.text
                R.id.exploreIndicaRB -> binding.exploreIndicaRB.text
                R.id.exploreSativaRB -> binding.exploreSativaRB.text
                else -> null
            }
            viewModel.updateGeneticType(geneticType.toString()) // Update genetic type in ViewModel
        }
    }

    /**
     * Sets up the THC slider to update the minimum THC level filter.
     */
    private fun setupTHCSlider() {
        binding.exploreTHCSlider.addOnChangeListener { _, thc, _ ->
            binding.exploreTHCTV.text = thc.toString() // Update THC level display
            viewModel.updateMinThc(thc) // Update minimum THC level in ViewModel
        }
    }

    /**
     * Sets up a scroll listener for the RecyclerView to show or hide the page-up button.
     */
    private fun setupScrollListener() {
        exploreRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val offset = recyclerView.computeVerticalScrollOffset()
                // Show or hide the page-up button based on scroll offset
                if (offset == 0) binding.explorePageUPBTN.hide() else binding.explorePageUPBTN.show()
            }
        })
        // Scroll to top when page-up button is clicked
        binding.explorePageUPBTN.setOnClickListener {
            exploreRV.smoothScrollToPosition(0)
        }
    }

    /**
     * Sets up MotionLayout transition listener to update the header text based on transition state.
     */
    private fun setupMotionLayoutListener() {
        binding.exploreML.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            /**
             * Called when a transition in MotionLayout is completed.
             * @param motionLayout The MotionLayout that completed the transition.
             * @param currentId The ID of the current end state.
             */
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                // Update header text based on transition state
                if (currentId == R.id.end || currentId == R.id.endFilter) {
                    binding.exploreHeaderTV.apply {
                        text = getString(R.string.explore)
                        textSize = 24f
                    }
                } else {
                    binding.exploreHeaderTV.apply {
                        text = getString(R.string.explore_header)
                        textSize = 42f
                    }
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })
    }

    /**
     * Handles click events on a genetic strain item to show details in a dialog.
     * @param plant The LocalGenetic item that was clicked.
     * @param view The view that was clicked.
     */
    override fun onGeneticClick(plant: LocalGenetic, view: View) {
        val dialogView = LayoutInflater.from(view.context)
            .inflate(R.layout.dialog_explore_detail, null, false)

        val headerTV = dialogView.findViewById<TextView>(R.id.exploreDetailHeaderTV)
        val detailTV = dialogView.findViewById<TextView>(R.id.exploreDetailTV)

        headerTV.setText(plant.strainName)
        detailTV.setText(buildDetailText(plant)) // Build and set the detail text

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Details")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    /**
     * Builds a detailed string representation of the plant's attributes.
     * @param plant The LocalGenetic item to build details for.
     * @return A string containing the details of the plant.
     */
    private fun buildDetailText(plant: LocalGenetic): String {
        return """
        Strain Type: ${plant.strainType}
        
        THC Level: ${plant.thcLevel}
        
        Effects: ${plant.effects}
        
        Description: ${plant.description}
        
        Terpene: ${plant.mostCommonTerpene}
    """.trimIndent()
    }

    override fun onPlantClick(masterPlantID: Long) {}

    override fun onTreatmentClick(masterPlantID: Long, view: View) {}

}
