package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.adapter.ExploreAdapter
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.models.record.LightRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.format.DateTimeFormatter

class ListLightAdapter :
    ListAdapter<LightRecord, ListLightAdapter.ListItemViewHolder>(DiffCallback) {

    inner class ListItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Light Adapter"

    // Formatter for displaying dates in the desired format
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        // Inflate the layout using the provided parent and return a new ViewHolder
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current LightRecord from the dataset
        val listEntry = getItem(position)

        // Use the view binding to set the data to the views
        with(holder.binding) {
            // Set the header for light status
            listItemHeader1TV.text = "Light ON/OFF"
            // Set the second header to null (not used)
            listItemHeader2TV.text = null
            // Display the number of hours the light was on and off (24 - lightHours)
            listItemTV1.text =
                listEntry.lightHours.toString() + " / " + (24 - listEntry.lightHours).toString()
            // Set the secondary text view to null (not used)
            listItem2TV.text = null
            // Format and set the date
            listItemDateTV.text = listEntry.date.format(dateFormatter)
        }
    }

    // Companion object to define a DiffUtil.ItemCallback for efficient list updates
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LightRecord>() {
            // Check if two items are the same (used for detecting item identity)
            override fun areItemsTheSame(oldItem: LightRecord, newItem: LightRecord): Boolean {
                return oldItem.id == newItem.id
            }

            // Check if the contents of two items are the same (used for detecting content changes)
            override fun areContentsTheSame(oldItem: LightRecord, newItem: LightRecord): Boolean {
                return oldItem == newItem
            }
        }
    }
}
