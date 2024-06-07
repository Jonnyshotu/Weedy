package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.MeasurementsRecord
import com.example.weedy.databinding.ListItemMeasurementsBinding

class ListMeasurementsAdapter (private val dataset: List<MeasurementsRecord>) : RecyclerView.Adapter<ListMeasurementsAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemMeasurementsBinding) : RecyclerView.ViewHolder(binding.root)

    private val TAG = "Debug_ListMeasurementsAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemMeasurementsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listEntry = dataset[position]

        with(holder.binding){
            listItemMeasureDateTV.text = listEntry.date.toString()
            listItemMeasureHeader1TV.text= "PH"
            listItemMeasureHeader2TV.text= "EC"
            listItemMeasureHeader3TV.text= "Temperature (°C)"
            listItemMeasureHeader4TV.text= "Humidity (%)"
            listItemMeasureTV1.text = listEntry.ph.toString()
            listItemMeasure2TV.text = listEntry.ec.toString()
            listItemMeasure3TV.text = listEntry.temperature.toString()
            listItemMeasure4TV.text = listEntry.humidity.toString()
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}