package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.TrainingRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.LocalDate

class ListTrainingAdapter (private val dataset: List<TrainingRecord>) : RecyclerView.Adapter<ListTrainingAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val TAG = "Debug_ListTrainingAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listEntry = dataset[position]
        with(holder.binding){
            listItemHeader1TV.text = "Training method"
            listItemHeader2TV.text = null
            listItemTV1.text = listEntry.trainingType
            listItem2TV.text = null
            listItemDateTV.text = listEntry.date.toString()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}