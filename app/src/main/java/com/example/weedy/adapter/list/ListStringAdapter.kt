package com.example.weedy.adapter.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.databinding.ListItemimpleStringBinding

class ListStringAdapter(
    private val dataset: List<String?>, // The dataset consisting of nullable strings
    private val onItemClicked: (String) -> Unit // Callback function to be invoked when an item is clicked
) : RecyclerView.Adapter<ListStringAdapter.ListItemViewHolder>() {

    private val TAG = "String Adapter"

    inner class ListItemViewHolder(val binding: ListItemimpleStringBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            ListItemimpleStringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current string from the dataset
        val listEntry = dataset[position]

        // Set the text view to display the string
        holder.binding.stringListItemTV.text = listEntry

        // Set up a click listener on the root view of the ViewHolder
        holder.binding.root.setOnClickListener {
            // Handle item click
            if (listEntry != null) {
                // Call the onItemClicked callback with the non-null string
                onItemClicked(listEntry)
                // Log the clicked item
                Log.d(TAG, "clicked list item: $listEntry")
            } else {
                // Call the onItemClicked callback with a default value if the string is null
                onItemClicked("Unknown")
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}
