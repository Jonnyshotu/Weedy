package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.RepotRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.format.DateTimeFormatter

class ListRepotAdapter(private val dataset: List<RepotRecord>) :
    RecyclerView.Adapter<ListRepotAdapter.ListItemViewHolder>() {

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
        val listEntry = dataset[position]

        // Use the view binding to set the data to the views
        with(holder.binding) {
            listItemHeader1TV.text = "Pot size" // Set header for pot size
            listItemHeader2TV.text = "Soil type" // Set header for soil type
            listItemTV1.text = listEntry.potSize.toString() // Set the pot size
            listItem2TV.text = listEntry.soilName // Set the soil type
            listItemDateTV.text = listEntry.date.format(dateFormatter) // Format and set the date
        }
    }

    // Returns the total number of items in the dataset
    override fun getItemCount(): Int {
        return dataset.size
    }
}
