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
import com.example.weedy.data.entities.Plant
import com.example.weedy.databinding.FragmentHomeBinding
import com.example.weedy.ui.main.MainFragment

class HomeFragment : MainFragment(), OnClick {

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

        if (!allPermissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.plantList.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.plantList.value)
            Log.d("$TAG Observer", viewModel.plantList.value.toString())

        }

        with(binding) {
            homeAddFAB.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewPlantFragment())
            }
        }
    }

    override fun onPlantClick(plant: Plant) {
        viewModel.navigatePlantID = plant.id
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
    }

    override fun onTreatmentClick(plant: Plant, view: View) {
        viewModel.navigatePlantID = plant.id
//        showTreatmentMenu(view)
    }

    override fun onImageCaptured(imageBitmap: Bitmap) {
        TODO()
    }

    override fun onImagePicked(imageUri: Uri?) {
        TODO()
    }
}