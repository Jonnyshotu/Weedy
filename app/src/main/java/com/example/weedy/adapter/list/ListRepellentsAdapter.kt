package com.example.weedy.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.data.models.record.RepellentsRecord
import com.example.weedy.databinding.ListItemBinding
import java.time.LocalDate

class ListRepellentsAdapter(private val dataset: List<RepellentsRecord>) :
    RecyclerView.Adapter<ListRepellentsAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val TAG = "Repellents Adapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listEntry = dataset[position]

        with(holder.binding) {
            listItemHeader1TV.text = "Incident"
            listItemHeader2TV.text = null
            listItemTV1.text = listEntry.infestationType
            listItem2TV.text = null
            listItemDateTV.text = listEntry.date.toString()
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}