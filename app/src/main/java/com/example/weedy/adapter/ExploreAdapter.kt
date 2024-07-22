package com.example.weedy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weedy.R
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.databinding.ListItemExploreBinding

class ExploreAdapter(
    private val listener: OnClick // Callback interface to handle item clicks
) : ListAdapter<LocalGenetic, ExploreAdapter.ListItemViewHolder>(DiffCallback) {

    private val TAG = "Explore Adapter"

    inner class ListItemViewHolder(val binding: ListItemExploreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            ListItemExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current LocalGenetic item from the list
        val listEntry = getItem(position)

        // Bind the data to the views
        with(holder.binding) {
            // Load the image from URL or set a default image if URL is empty
            if (listEntry.strainImageURL.isNullOrEmpty()) {
                exploreItemIV.setImageResource(R.drawable.example_plant) // Default image
            } else {
                exploreItemIV.load(listEntry.strainImageURL) // Load image from URL using Coil
            }
            // Set the strain name, type, THC level, and most common terpene
            exploreItemStrainTV.text = listEntry.strainName
            exploreItemTypeTV.text = listEntry.strainType
            exploreItemThcTV.text = "${listEntry.thcLevel} THC"
            exploreItemTerpeneTV.text = listEntry.mostCommonTerpene

            // Set up a click listener on the card view
            exploreListItemCV.setOnClickListener { view ->
                // Notify the listener of the item click
                listener.onGeneticClick(listEntry, view)
            }
        }
    }

    // Companion object to define a DiffUtil.ItemCallback for efficient list updates
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LocalGenetic>() {
            // Check if two items are the same (used for detecting item identity)
            override fun areItemsTheSame(oldItem: LocalGenetic, newItem: LocalGenetic): Boolean {
                return oldItem.id == newItem.id
            }

            // Check if the contents of two items are the same (used for detecting content changes)
            override fun areContentsTheSame(oldItem: LocalGenetic, newItem: LocalGenetic): Boolean {
                return oldItem == newItem
            }
        }
    }
}
