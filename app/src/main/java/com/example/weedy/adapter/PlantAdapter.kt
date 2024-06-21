package com.example.weedy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.databinding.PlantItemBinding

class PlantAdapter(
    private val listener: OnClick
) : ListAdapter<MasterPlant, PlantAdapter.ItemViewHolder>(PlantDiffUtil()) {

    inner class ItemViewHolder(val binding: PlantItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Plant Adapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = PlantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val plant = getItem(position)

        Log.d("$TAG on BindView", plant.toString())

        with(holder.binding) {
            plantItemActionBTN.setOnClickListener {
                listener.onTreatmentClick(plant, this.plantItemActionBTN)
            }
            plantItemStrainTV.text = plant.strainName

            plantItemCV.setOnClickListener {
                listener.onPlantClick(plant)
            }
        }
    }
}
