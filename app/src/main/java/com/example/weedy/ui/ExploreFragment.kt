package com.example.weedy.ui

import com.example.weedy.adapter.ExploreAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.ExploreViewModel
import com.example.weedy.R
import com.example.weedy.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private val TAG = "Explore Fragment"

    private lateinit var binding: FragmentExploreBinding
    private lateinit var exploreRV: RecyclerView
    private lateinit var adapter: ExploreAdapter

    private val viewModel: ExploreViewModel by activityViewModels()

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

        viewModel.filteredGeneticList.observe(viewLifecycleOwner) { filteredList ->
            adapter.submitList(filteredList)
        }
    }

    private fun setupRecyclerView() {
        exploreRV = binding.exploreRV
        adapter = ExploreAdapter()
        exploreRV.adapter = adapter
    }

    private fun setupResetBTN(){
        binding.exploreResetBTN.setOnClickListener {
            binding.exploreSearchET.text = null
            binding.exploreRG.clearCheck()
            binding.exploreTHCSlider.value = 0F
        }
    }

    private fun setupSearchInput() {
        binding.exploreSearchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.updateSearchInput(s?.toString())
            }
        })
    }

    private fun setupGeneticTypeRadioGroup() {
        binding.exploreRG.setOnCheckedChangeListener { group, checkedId ->
            val geneticType = when (checkedId) {
                R.id.exploreHybridRB -> binding.exploreHybridRB.text
                R.id.exploreIndicaRB -> binding.exploreIndicaRB.text
                R.id.exploreSativaRB -> binding.exploreSativaRB.text
                else -> null
            }
            viewModel.updateGeneticType(geneticType.toString())
        }
    }

    private fun setupTHCSlider() {
        binding.exploreTHCSlider.addOnChangeListener { _, thc, _ ->
            binding.exploreTHCTV.text = thc.toString()
            viewModel.updateMinThc(thc)
        }
    }

    private fun setupScrollListener() {
        exploreRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val offset = recyclerView.computeVerticalScrollOffset()
                if (offset == 0) binding.explorePageUPBTN.hide() else binding.explorePageUPBTN.show()
            }
        })
        binding.explorePageUPBTN.setOnClickListener {
            exploreRV.smoothScrollToPosition(0)
        }
    }

    private fun setupMotionLayoutListener() {
        binding.exploreML.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
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
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })
    }
}