package com.example.weedy.adapter

import android.view.View
import com.example.weedy.data.module.Plant

interface OnClick {

    fun onTreatmentClick(plant: Plant, view: View)
}