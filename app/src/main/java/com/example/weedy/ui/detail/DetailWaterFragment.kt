package com.example.weedy.ui.detail

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.adapter.list.ListWaterAdapter
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.FragmentDetailHomeBinding
import com.example.weedy.databinding.FragmentDetailWaterBinding
import com.example.weedy.databinding.FragmentNewPlantGeneticBinding
import com.example.weedy.ui.main.MainFragment
import com.example.weedy.ui.new_plant.NewPlantGeneticFragmentArgs
import com.example.weedy.ui.new_plant.NewPlantGeneticFragmentDirections


class DetailWaterFragment : MainFragment() {

    private val TAG = "Detail Water Fragment"
    private lateinit var binding: FragmentDetailWaterBinding
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var plant: MasterPlant
    private lateinit var waterRecyclerView: RecyclerView

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

        // Observe the plant data
        viewModel.plant.observe(viewLifecycleOwner) { masterPlant ->
            plant = masterPlant

            viewModel.getWateringRecordByPlantID(plant.id)
                .observe(viewLifecycleOwner) { wateringRecords ->
                    Log.d(TAG, "Watering Recordsize: ${wateringRecords.size}")
                    waterRecyclerView.adapter = ListWaterAdapter(wateringRecords)
                }
        }

        waterRecyclerView = binding.detailWaterRV

    }

    override fun onImageCaptured(imageBitmap: Bitmap) {
    }

    override fun onImagePicked(imageUri: Uri?) {
    }
}
