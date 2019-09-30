package com.example.upside.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.upside.model.*
import com.example.upside.services.IUpsideApiService
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.StringReader

class UpsideClient {
    companion object {
        private fun getUpsideClientInstance() : IUpsideApiService { // TODO: make into singleton
            val retrofit = Retrofit.Builder()
                // .addConverterFactory(GsonConverterFactory.create()) // TODO: Should be using this one instead but it isn't quite working right
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://native-apps-assessment.upside.com")
                .build()

            return retrofit.create(IUpsideApiService::class.java)
        }


        fun getDeals() : MutableLiveData<Deals> {
            val mutableLiveData = MutableLiveData<Deals>()

            // fetch deals
            val dealsCall = getUpsideClientInstance().getDeals()
            dealsCall.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MainActivity", "Failed : ".plus(t.message))
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val strReader = StringReader(response.body()!!)
                    val gsonBuilder = GsonBuilder().serializeNulls()
                    gsonBuilder.registerTypeAdapter(Deals::class.java, DealsDeserializer())

                    val gson = gsonBuilder.create()
                    mutableLiveData.value = gson.fromJson(strReader , Deals::class.java)
                }
            })

            return mutableLiveData
        }

        fun getCurrencies() : MutableLiveData<Currencies> {
            val mutableLiveData = MutableLiveData<Currencies>()

            val currencyCall = getUpsideClientInstance().getCurrencies()
            currencyCall.enqueue(object:Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MainActivity", "Failed : ".plus(t.message))
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (!response.isSuccessful) {
                        Log.e("MainActivity", "onResponse - failed")
                        return
                    }

                    val strReader = StringReader(response.body()!!)
                    val gsonBuilder = GsonBuilder().serializeNulls()
                    gsonBuilder.registerTypeAdapter(Currencies::class.java, CurrencyDeserializer())

                    val gson = gsonBuilder.create()
                    mutableLiveData.value = gson.fromJson(strReader , Currencies::class.java)
                }
            })

            return mutableLiveData
        }

        fun getHotels() : MutableLiveData<Hotels> {
            val mutableLiveData = MutableLiveData<Hotels>()

            val hotelCall = getUpsideClientInstance().getHotels()
            hotelCall.enqueue(object:Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MainActivity", "Failed : ".plus(t.message))
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (!response.isSuccessful) {
                        Log.e("MainActivity", "onResponse - failed")
                        return
                    }

                    val strReader = StringReader(response.body()!!)
                    val gsonBuilder = GsonBuilder().serializeNulls()
                    gsonBuilder.registerTypeAdapter(Hotels::class.java, HotelDeserializer())

                    val gson = gsonBuilder.create()
                    mutableLiveData.value = gson.fromJson(strReader , Hotels::class.java)
                }
            })

            return mutableLiveData
        }

        fun getAirports() : MutableLiveData<Airports> {
            val mutableLiveData = MutableLiveData<Airports>()

            val airportsCall = getUpsideClientInstance().getAirports()
            airportsCall.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MainActivity", "Failed : ".plus(t.message))
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (!response.isSuccessful) {
                        Log.e("MainActivity", "onResponse - failed")
                        return
                    }

                    val strReader = StringReader(response.body()!!)
                    val gsonBuilder = GsonBuilder().serializeNulls()
                    gsonBuilder.registerTypeAdapter(Airports::class.java, AirportDeserializer())

                    val gson = gsonBuilder.create()
                    mutableLiveData.value = gson.fromJson(strReader , Airports::class.java)
                }
            })

            return mutableLiveData
        }

        // TODO: handle generic types
        fun getAirlines() : MutableLiveData<Airlines> {
            val mutableLiveData = MutableLiveData<Airlines>()

            val airlinesCall = getUpsideClientInstance().getAirlines()
            airlinesCall.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MainActivity", "Failed : ".plus(t.message))
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (!response.isSuccessful) {
                        Log.e("MainActivity", "onResponse - failed")
                        return
                    }

                    val strReader = StringReader(response.body()!!)
                    val gsonBuilder = GsonBuilder().serializeNulls()
                    gsonBuilder.registerTypeAdapter(Airlines::class.java, AirlineDeserializer())

                    val gson = gsonBuilder.create()
                    mutableLiveData.value = gson.fromJson(strReader , Airlines::class.java)
                }
            })

            return mutableLiveData
        }
    }
}