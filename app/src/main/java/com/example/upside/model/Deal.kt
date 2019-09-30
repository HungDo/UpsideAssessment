package com.example.upside.model

import com.google.gson.*
import java.lang.reflect.Type

class Deal(val price:Double,
           val currency:String,
           val flights:Map<String, Flight>,
           val hotel:HotelStay) {
    constructor() : this(0.0, "", HashMap<String, Flight>(), HotelStay("", 0))

    override fun toString(): String {
        return "Deal(price=$price, currency='$currency', flights=${flights.size}, hotel=${hotel.nights})"
    }
}

class Deals(val list:List<Deal>)

class HotelStay(val brand:String, val nights:Int)

class DealsDeserializer : JsonDeserializer<Deals> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Deals? {
        val jsonArry = json as JsonArray

        val deals = ArrayList<Deal>()

        // loop through json elements
        for (idx in jsonArry) {
            val jsonObj = idx.asJsonObject

            val price = jsonObj.getAsJsonPrimitive("price").asDouble
            val currency = jsonObj.getAsJsonPrimitive("currency").asString

            // parse flights
            val out = deserializeFlight("outbound", jsonObj.getAsJsonObject("flights"))
            val ret = deserializeFlight("return", jsonObj.getAsJsonObject("flights"))
            val flights = HashMap<String, Flight>()
            flights.put("outbound", out)
            flights.put("return", ret)

            // parse hotel
            val hotel = deserializeHotel(jsonObj.getAsJsonObject("hotel"))

            deals.add(Deal(price, currency, flights, hotel))
        }

        return Deals(deals)
    }

    private fun deserializeHotel(json: JsonElement?) : HotelStay {
        val jsonObj = json as JsonObject
        return HotelStay(jsonObj.getAsJsonPrimitive("brand").asString, jsonObj.getAsJsonPrimitive("nights").asInt)
    }

    private fun deserializePosition(type:String, json: JsonElement?) : Position {
        val jsonObj = json as JsonObject
        val start = jsonObj.getAsJsonObject(type)

        val datetime = start.getAsJsonPrimitive("datetime").asString
        val airport = start.getAsJsonPrimitive("airport").asString

        return Position(datetime.toString(), airport.toString())
    }

    private fun deserializeFlight(type:String, json: JsonElement?) : Flight {
        val jsonObj = json as JsonObject

        val flight = jsonObj.getAsJsonObject(type)
        val airline = flight.getAsJsonPrimitive("airline").asString

        // parse Position
        val start = deserializePosition("start", flight)
        val end = deserializePosition("end", flight)

        return Flight(airline.toString(), start, end) // add start
    }
 }