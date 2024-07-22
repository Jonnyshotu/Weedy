package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.NutrientsRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.format.DateTimeFormatter

class ListNutrientsAdapter(private val dataset: List<NutrientsRecord>) :
    RecyclerView.Adapter<ListNutrientsAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Nutrients Adapter"

    // Formatter for displaying dates in the desired format
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        // Inflate the layout using the provided parent and return a new ViewHolder
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current NutrientsRecord from the dataset
        val listEntry = dataset[position]

        // Use the view binding to set the data to the views
        with(holder.binding) {
            listItemHeader1TV.text = "Type" // Set the header for the nutrient type
            listItemHeader2TV.text = "Amount" // Set the header for the nutrient amount
            listItemTV1.text = listEntry.nutrientName // Set the nutrient name
            listItem2TV.text = listEntry.amount.toString() // Set the amount of the nutrient
            listItemDateTV.text = listEntry.date.format(dateFormatter) // Format and set the date
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}
