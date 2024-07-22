package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.LightRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.format.DateTimeFormatter

class ListLightAdapter(private val dataset: List<LightRecord>) :
    RecyclerView.Adapter<ListLightAdapter.ListItemViewHolder>() {

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
        val listEntry = dataset[position]

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

    override fun getItemCount(): Int {
        return dataset.size
    }
}
