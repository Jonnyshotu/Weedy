package com.example.weedy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.adapter.OnClick
import com.example.weedy.adapter.PlantAdapter
import com.example.weedy.data.module.Plant
import com.example.weedy.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), OnClick {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PlantAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: SharedViewModel by activityViewModels()

    private val TAG = "Debug_HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater)

        recyclerView = binding.homeRV
        adapter = PlantAdapter(this)

        recyclerView.adapter = adapter

        viewModel.loadExamplePlants()
        adapter.submitList(viewModel.plants.value)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(R.menu.treatment_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.checkup_option -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCheckupFragment())
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onTreatmentClick(plant: Plant, view: View) {
        viewModel.navigatePlant = plant
        showMenu(view)
    }
}