package com.example.weedy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.R
import com.example.weedy.data.models.DisplayPlant
import com.example.weedy.databinding.ListItemPlantBinding

class PlantAdapter(
    private val listener: OnClick, // Callback interface to handle item clicks
) : ListAdapter<DisplayPlant, PlantAdapter.ItemViewHolder>(DiffCallback) {

    private val TAG = "Plant Adapter"

    inner class ItemViewHolder(val binding: ListItemPlantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ListItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Get the current DisplayPlant item from the list
        val plant = getItem(position)

        // Bind the data to the views
        with(holder.binding) {
            // Set click listener for the action button
            plantItemActionBTN.setOnClickListener { view ->
                listener.onTreatmentClick(plant.masterPlantID, view)
            }
            // Set the strain name
            plantItemStrainTV.text = plant.strainName

            // Set the health image based on the health percentage
            plant.health.let { percentage ->
                when (percentage) {
                    100 -> plantItemHealthIV.setImageResource(R.drawable.health_100)
                    80 -> plantItemHealthIV.setImageResource(R.drawable.health_80)
                    60 -> plantItemHealthIV.setImageResource(R.drawable.health_60)
                    40 -> plantItemHealthIV.setImageResource(R.drawable.health_40)
                    20 -> plantItemHealthIV.setImageResource(R.drawable.health_20)
                    0 -> plantItemHealthIV.setImageResource(R.drawable.health_0)
                    else -> plantItemHealthIV.setImageResource(R.drawable.no_data)
                }
            }

            // Set the growth state text based on the growth state value
            plant.growthState.let { growthState ->
                when (growthState) {
                    5 -> plantItemGrowthStateTV.setText(R.string.flower)
                    4 -> plantItemGrowthStateTV.setText(R.string.vegetation)
                    3 -> plantItemGrowthStateTV.setText(R.string.cutting)
                    2 -> plantItemGrowthStateTV.setText(R.string.seedling)
                    1 -> plantItemGrowthStateTV.setText(R.string.germination)
                    else -> plantItemGrowthStateTV.setText(R.string.no_data)
                }
            }

            // Update progress bar based on the plant's age and flower time
            plant.flowerTime?.let { flowerTime ->
                val flowerTimeInDays = flowerTime * 7
                val age = plant.age

                if (age != null) {
                    with(plantItemAgePB) {
                        max = flowerTimeInDays
                        progress = age
                    }
                } else {
                    plantItemAgePB.progress = 0
                }
            }

            // Set click listener for the card view
            plantItemCV.setOnClickListener {
                listener.onPlantClick(plant.masterPlantID)
            }
        }

        // Log the details of the plant for debugging
        Log.d(
            TAG,
            "${plant.strainName} Health: ${plant.health}, Age: ${plant.age}, Flower Time: ${plant.flowerTime}, Latest Growth State: ${plant.growthState}"
        )
    }

    // Companion object to define a DiffUtil.ItemCallback for efficient list updates
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DisplayPlant>() {
            // Check if two items are the same (used for detecting item identity)
            override fun areItemsTheSame(oldPlant: DisplayPlant, newPlant: DisplayPlant): Boolean {
                Log.d(
                    "DiffCallback items",
                    "${oldPlant.masterPlantID} and ${newPlant.masterPlantID}"
                )
                return oldPlant.masterPlantID == newPlant.masterPlantID
            }

            // Check if the contents of two items are the same (used for detecting content changes)
            override fun areContentsTheSame(
                oldPlant: DisplayPlant,
                newPlant: DisplayPlant
            ): Boolean {
                Log.d(
                    "DiffCallback contents",
                    "${oldPlant.strainName} ${oldPlant.health} and ${newPlant.strainName} ${newPlant.health}: ${oldPlant == newPlant}"
                )
                return oldPlant == newPlant
            }
        }
    }
}
