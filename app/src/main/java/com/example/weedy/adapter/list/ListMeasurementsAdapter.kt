package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.MeasurementsRecord
import com.example.weedy.databinding.ListItemMeasurementsBinding
import java.time.format.DateTimeFormatter

class ListMeasurementsAdapter(private val dataset: List<MeasurementsRecord>) :
    RecyclerView.Adapter<ListMeasurementsAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemMeasurementsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Measurements Adapter"

    // Formatter for displaying dates in the desired format
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        // Inflate the layout using the provided parent and return a new ViewHolder
        val binding =
            ListItemMeasurementsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Get the current MeasurementsRecord from the dataset
        val listEntry = dataset[position]

        // Use the view binding to set the data to the views
        with(holder.binding) {
            listItemMeasureDateTV.text =
                listEntry.date.format(dateFormatter) // Format and set the date
            listItemMeasureHeader1TV.text = "PH" // Set the header for pH value
            listItemMeasureHeader2TV.text =
                "EC" // Set the header for Electrical Conductivity (EC) value
            listItemMeasureHeader3TV.text = "Temperature (°C)" // Set the header for temperature
            listItemMeasureHeader4TV.text = "Humidity (%)" // Set the header for humidity
            listItemMeasureTV1.text = listEntry.ph.toString() // Set the pH value
            listItemMeasure2TV.text = listEntry.ec.toString() // Set the EC value
            listItemMeasure3TV.text = listEntry.temperature.toString() // Set the temperature value
            listItemMeasure4TV.text = listEntry.humidity.toString() // Set the humidity value
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}
