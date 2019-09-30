package com.example.upside.services

import retrofit2.Call
import retrofit2.http.GET

interface IUpsideApiService {

    @GET("/airports.json")
    fun getAirports() : Call<String> // Call<String> can be improved to List<Airport>

    @GET("/airlines.json")
    fun getAirlines() : Call<String>

    @GET("/hotels.json")
    fun getHotels() : Call<String>

    @GET("/deals.json")
    fun getDeals() : Call<String>

    @GET("/currencies.json")
    fun getCurrencies() : Call<String>

}