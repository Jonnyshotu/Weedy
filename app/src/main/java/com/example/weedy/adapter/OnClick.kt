package com.example.weedy.adapter

import android.view.View
import com.example.weedy.data.models.Plant

interface OnClick {

    fun onPlantClick(plant: Plant)
    fun onTreatmentClick(plant: Plant, view: View)
}