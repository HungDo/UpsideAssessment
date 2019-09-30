package com.example.upside.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.upside.model.Hotels
import com.example.upside.network.UpsideClient

class HotelsViewModel : ViewModel() {
    var hotels : MutableLiveData<Hotels>? = null

    fun initialize() {
        hotels = UpsideClient.getHotels()
    }
}