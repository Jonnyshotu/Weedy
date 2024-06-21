package com.example.weedy.adapter.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weedy.databinding.SimpleStringItemBinding

class ListStringAdapter(
    private val dataset: List<String?>,
    val onItemClicked: (String) -> Unit,
) :
    RecyclerView.Adapter<ListStringAdapter.ListItemViewHolder>() {

    private val TAG = "String Adapter"

    inner class ListItemViewHolder(val binding: SimpleStringItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            SimpleStringItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStringAdapter.ListItemViewHolder, position: Int) {
        val listEntry = dataset[position]

        holder.binding.stringListItemTV.text = listEntry

        holder.binding.root.setOnClickListener {
            if (listEntry != null) {
                onItemClicked(listEntry)
                Log.d(TAG, "clicked list item: $listEntry")
            } else onItemClicked("Unknown")
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}