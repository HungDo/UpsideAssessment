package com.example.upside.model

import com.google.gson.*
import java.lang.reflect.Type

class Airport(val _id:String, val name:String, val city:String) {
    override fun toString(): String {
        return "{$_id - $name}"
    }
}

class Airports(val map:Map<String, Airport>)

class AirportDeserializer : JsonDeserializer<Airports> {
    private val cities = arrayOf("SAN", "IAD", "SEA", "JFK", "ATL", "BKK", "CNX", "HKT", "NRT", "UKB", "TXL", "LHR")

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Airports? {
        val jsonObj = json as JsonObject
        val airports = LinkedHashMap<String, Airport>()

        val gson = Gson()
        for (c in cities) {
            val jsonCity = jsonObj.getAsJsonObject(c)
            val airport = gson.fromJson(jsonCity, Airport::class.java)
            airports[airport._id] = airport
        }

        return Airports(airports)
    }
}