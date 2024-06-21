package com.example.weedy.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.weedy.data.entities.MasterPlant

class PlantDiffUtil : DiffUtil.ItemCallback<MasterPlant>() {

    private val TAG = "Debug_PlantDiffUtil"
    override fun areItemsTheSame(oldPlant: MasterPlant, newPlant: MasterPlant): Boolean {
        val result = oldPlant.id == newPlant.id
        Log.d(TAG, "result: $result oldPlant: $oldPlant, newPlant: $newPlant")
        return result
    }

    override fun areContentsTheSame(oldPlant: MasterPlant, newPlant: MasterPlant): Boolean {
        val result = oldPlant == newPlant
        Log.d(TAG, "result: $result oldPlant: $oldPlant, newPlant: $newPlant")
        return result
    }
}