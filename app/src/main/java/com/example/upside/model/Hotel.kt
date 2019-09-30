package com.example.upside.model

import com.google.gson.*
import java.lang.reflect.Type

class Hotel(val _id:String, val name:String, val rating:Float, val image_url:String) {
    override fun toString(): String {
        return "Hotel(_id='$_id', name='$name', rating=$rating, image_url='$image_url')"
    }
}

class Hotels(val map: Map<String, Hotel>)

class HotelDeserializer : JsonDeserializer<Hotels> {
    // TODO: theres a better way to do this
    private val hotelNames = arrayOf("marriott", "novotel", "hyatt")

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Hotels? {
        val jsonObj = json as JsonObject
        val hotels = LinkedHashMap<String, Hotel>()

        val gson = Gson()
        for (h in hotelNames) {
            val jsonHotel = jsonObj.getAsJsonObject(h)
            val hotel = gson.fromJson(jsonHotel, Hotel::class.java)
            hotels[hotel._id] = hotel
        }

        return Hotels(hotels)
    }
}