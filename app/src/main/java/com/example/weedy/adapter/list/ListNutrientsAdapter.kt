package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.databinding.ListItemBinding
import java.time.LocalDate

class ListNutrientsAdapter (private val dataset: List<Triple<String, Double, LocalDate>>) : RecyclerView.Adapter<ListNutrientsAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val TAG = "Debug_ListNutrientsAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listEntry = dataset[position]

        with(holder.binding){
            listItemHeader1TV.text = "Type"
            listItemHeader2TV.text = "Amount"
            listItemTV1.text = listEntry.first
            listItem2TV.text = listEntry.second.toString()
            listItemDateTV.text = listEntry.third.toString()
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}