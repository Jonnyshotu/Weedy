package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.WateringRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.LocalDate

class ListWaterAdapter(private val dataset: List<WateringRecord>) : RecyclerView.Adapter<ListWaterAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val TAG = "Water Adapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listEntry = dataset[position]

        with(holder.binding){
            listItemHeader1TV.text = "Water in ml"
            listItemHeader2TV.text = null
            listItemTV1.text = listEntry.amount.toString()
            listItem2TV.text = listEntry.ph.toString()
            listItemDateTV.text = listEntry.date.toString()
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}