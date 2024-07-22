package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.adapter.ExploreAdapter
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.models.record.RepotRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.format.DateTimeFormatter

class ListRepotAdapter :
    ListAdapter<RepotRecord, ListRepotAdapter.ListItemViewHolder>(DiffCallback) {

    inner class ListItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Repot Adapter"

    // Formatter for displaying dates in the desired format
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        // Inflate the layout using the provided parent and return a new ViewHolder
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current RepotRecord from the dataset
        val listEntry = getItem(position)

        // Use the view binding to set the data to the views
        with(holder.binding) {
            listItemHeader1TV.text = "Pot size" // Set header for pot size
            listItemHeader2TV.text = "Soil type" // Set header for soil type
            listItemTV1.text = listEntry.potSize.toString() // Set the pot size
            listItem2TV.text = listEntry.soilName // Set the soil type
            listItemDateTV.text = listEntry.date.format(dateFormatter) // Format and set the date
        }
    }

    // Companion object to define a DiffUtil.ItemCallback for efficient list updates
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RepotRecord>() {
            // Check if two items are the same (used for detecting item identity)
            override fun areItemsTheSame(oldItem: RepotRecord, newItem: RepotRecord): Boolean {
                return oldItem.id == newItem.id
            }

            // Check if the contents of two items are the same (used for detecting content changes)
            override fun areContentsTheSame(oldItem: RepotRecord, newItem: RepotRecord): Boolean {
                return oldItem == newItem
            }
        }
    }
}
