package com.example.upside.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.upside.model.Airlines
import com.example.upside.network.UpsideClient

class AirlinesViewModel : ViewModel() {
    var airlines : MutableLiveData<Airlines>? = null

    fun initialize() {
        airlines = UpsideClient.getAirlines()
    }
}