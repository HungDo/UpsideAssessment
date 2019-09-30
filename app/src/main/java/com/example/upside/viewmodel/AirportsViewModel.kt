package com.example.upside.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.upside.model.Airports
import com.example.upside.network.UpsideClient

class AirportsViewModel : ViewModel() {
    var airports : MutableLiveData<Airports>? = null

    fun initialize() {
        airports = UpsideClient.getAirports()
    }
}