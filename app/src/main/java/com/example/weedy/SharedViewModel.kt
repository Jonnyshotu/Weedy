package com.example.weedy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weedy.data.Repository
import com.example.weedy.data.module.Plant

class SharedViewModel : ViewModel() {

    lateinit var navigatePlant : Plant

    private var _plants = MutableLiveData<List<Plant>>()
    val plants : LiveData<List<Plant>>
        get() = _plants

    fun loadExamplePlants(){
        _plants.value = Repository.loadExamplePlants()
    }
}
