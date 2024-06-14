package com.example.weedy.ui

import ListExploreAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.weedy.SharedViewModel
import com.example.weedy.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var adapter: ListExploreAdapter
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.localGeneticCollection.observe(viewLifecycleOwner) { genetics ->
            adapter = ListExploreAdapter(genetics)
            binding.exploreRV.adapter = adapter
        }
    }
}