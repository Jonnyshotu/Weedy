package com.example.weedy.adapter.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.adapter.ExploreAdapter
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.models.record.WateringRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListWaterAdapter :
    ListAdapter<WateringRecord, ListWaterAdapter.ListItemViewHolder>(DiffCallback) {

    inner class ListItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Water Adapter"

    // Formatter for displaying dates in the desired format
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current WateringRecord from the dataset
        val listEntry = getItem(position)

        Log.d(TAG,"Adapter List size: ${currentList.size}")

        // Use the view binding to set the data to the views
        with(holder.binding) {
            listItemHeader1TV.text = "Water in ml" // Set header for amount of water
            listItemHeader2TV.text =
                if (listEntry.ph != null) "PH" else null // Set header for pH if available
            listItemTV1.text = listEntry.amount.toString() // Set the amount of water
            listItem2TV.text =
                if (listEntry.ph != null) listEntry.ph.toString() else null // Set the pH value if available
            listItemDateTV.text = listEntry.date.format(dateFormatter) // Format and set the date
        }
    }

    // Companion object to define a DiffUtil.ItemCallback for efficient list updates
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<WateringRecord>() {
            // Check if two items are the same (used for detecting item identity)
            override fun areItemsTheSame(
                oldItem: WateringRecord,
                newItem: WateringRecord
            ): Boolean {
                return oldItem.id == newItem.id
            }

            // Check if the contents of two items are the same (used for detecting content changes)
            override fun areContentsTheSame(
                oldItem: WateringRecord,
                newItem: WateringRecord
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
