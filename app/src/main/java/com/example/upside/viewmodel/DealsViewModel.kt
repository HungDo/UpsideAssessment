package com.example.upside.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.upside.model.Deals
import com.example.upside.network.UpsideClient

class DealsViewModel : ViewModel() {
    var deals : MutableLiveData<Deals>? = null

    fun initialize() {
        deals = UpsideClient.getDeals()
    }
}