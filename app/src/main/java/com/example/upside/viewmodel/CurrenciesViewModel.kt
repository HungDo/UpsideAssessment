package com.example.upside.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.upside.model.Currencies
import com.example.upside.network.UpsideClient

class CurrenciesViewModel : ViewModel() {
    var currencies : MutableLiveData<Currencies>? = null

    fun initialize() {
        currencies = UpsideClient.getCurrencies()
    }
}