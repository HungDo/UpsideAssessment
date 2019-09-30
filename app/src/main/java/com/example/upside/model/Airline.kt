package com.example.upside.model

import android.util.Log
import com.google.gson.*
import java.lang.reflect.Type

class Airline(val _id:String, val name:String, val image_url:String) {
    override fun toString(): String {
        return "{$_id - $name}"
    }
}

class Airlines(val map:Map<String, Airline>)

class AirlineDeserializer : JsonDeserializer<Airlines> {
    private val airlineNames = arrayOf("aa", "ua", "b6", "vx", "dl", "as")

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Airlines? {
        val jsonObj = json as JsonObject
        val airlines = LinkedHashMap<String, Airline>()

        val gson = Gson()
        for (a in airlineNames) {
            val jsonAirline = jsonObj.getAsJsonObject(a)
            val airline = gson.fromJson(jsonAirline, Airline::class.java)
            airlines[airline._id] = airline
        }

        return  Airlines(airlines)
    }
}