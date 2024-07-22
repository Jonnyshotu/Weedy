package com.example.weedy.adapter

import android.view.View
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.data.entities.MasterPlant

interface OnClick {

    /**
     *Called when a plant item is clicked.
     *@param masterPlantID The unique ID of the plant that was clicked.
     */
    fun onPlantClick(masterPlantID: Long)

    /**
     *Called when a genetic item is clicked.
     *@param plant The LocalGenetic object representing the clicked genetic item.
     * @param view: The view that was clicked.
     */
    fun onGeneticClick(plant: LocalGenetic, view: View)

    /**
     *Called when a treatment action is triggered.
     *@param masterPlantID: The unique ID of the plant associated with the treatment.
     * @param view: The view that triggered the treatment action.
     */
    fun onTreatmentClick(masterPlantID: Long, view: View)
}
