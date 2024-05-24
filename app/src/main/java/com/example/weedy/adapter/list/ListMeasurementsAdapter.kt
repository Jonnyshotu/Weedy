package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.module.Measurements
import com.example.weedy.databinding.ListItemMeasurementsBinding
import java.time.LocalDate

class ListMeasurementsAdapter (private val dataset: List<Pair<Measurements, LocalDate>>) : RecyclerView.Adapter<ListMeasurementsAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemMeasurementsBinding) : RecyclerView.ViewHolder(binding.root)

    private val TAG = "Debug_ListMeasurementsAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemMeasurementsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listEntry = dataset[position]

        with(holder.binding){
            listItemMeasureDateTV.text = listEntry.second.toString()
            listItemMeasureHeader1TV.text= "PH"
            listItemMeasureHeader2TV.text= "EC"
            listItemMeasureHeader3TV.text= "Temperature (°C)"
            listItemMeasureHeader4TV.text= "Humidity (%)"
            listItemMeasureTV1.text = listEntry.first.ph.toString()
            listItemMeasure2TV.text = listEntry.first.ec.toString()
            listItemMeasure3TV.text = listEntry.first.temperature.toString()
            listItemMeasure4TV.text = listEntry.first.humidity.toString()
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}