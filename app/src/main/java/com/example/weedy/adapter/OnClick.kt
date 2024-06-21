package com.example.weedy.adapter

import android.view.View
import com.example.weedy.data.entities.MasterPlant

interface OnClick {

    fun onPlantClick(plant: MasterPlant)
    fun onTreatmentClick(plant: MasterPlant, view: View)
}