package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.TrainingRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListTrainingAdapter(private val dataset: List<TrainingRecord>) :
    RecyclerView.Adapter<ListTrainingAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Training Adapter"

    // Formatter for displaying dates in the desired format
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current TrainingRecord from the dataset
        val listEntry = dataset[position]

        // Use the view binding to set the data to the views
        with(holder.binding) {
            listItemHeader1TV.text = "Training method" // Set header for training method
            listItemHeader2TV.text = null // Header 2 is not used in this case
            listItemTV1.text = listEntry.trainingType // Set the training method type
            listItem2TV.text = null // Secondary text view is not used in this case
            listItemDateTV.text = listEntry.date.format(dateFormatter) // Format and set the date
        }
    }

    // Returns the total number of items in the dataset
    override fun getItemCount(): Int {
        return dataset.size
    }
}
