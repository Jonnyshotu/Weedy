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

class ExploreAdapter : ListAdapter<LocalGenetic, ExploreAdapter.ListItemViewHolder>(DiffCallback) {

    private val TAG = "Explore Adapter"

    inner class ListItemViewHolder(val binding: ListItemExploreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            ListItemExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listEntry = getItem(position)

        with(holder.binding) {
            if (listEntry.strainImageURL.isNullOrEmpty()) {
                exploreItemIV.setImageResource(R.drawable.example_plant)
            } else {
                exploreItemIV.load(listEntry.strainImageURL)
            }
            exploreItemStrainTV.text = listEntry.strainName
            exploreItemTypeTV.text = listEntry.strainType
            exploreItemThcTV.text = "${listEntry.thcLevel} THC"
            exploreItemTerpeneTV.text = listEntry.mostCommonTerpene
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LocalGenetic>() {
            override fun areItemsTheSame(oldItem: LocalGenetic, newItem: LocalGenetic): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LocalGenetic, newItem: LocalGenetic): Boolean {
                return oldItem == newItem
            }
        }
    }
}