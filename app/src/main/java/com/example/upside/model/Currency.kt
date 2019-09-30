package com.example.upside.model

import com.google.gson.*
import java.lang.reflect.Type

class Currency(val _id:String, val name:String, val symbol:String, val exchange:Double) {
    override fun toString(): String {
        return "{$_id - $name}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Currency

        if (_id != other._id) return false

        return true
    }

    override fun hashCode(): Int {
        return _id.hashCode()
    }
}

class Currencies(val map: Map<String, Currency>)

class CurrencyDeserializer : JsonDeserializer<Currencies> {
    companion object {
        val currencyNames = arrayOf("USD", "BHT", "EUR", "YEN")
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Currencies? {
        val jsonObj = json as JsonObject
        val currencies = LinkedHashMap<String, Currency>()

        val gson = Gson()
        for (c in currencyNames) {
            val jsonCurr = jsonObj.getAsJsonObject(c)
            val currency = gson.fromJson(jsonCurr, Currency::class.java)
            currencies[currency._id] = currency
        }

        return Currencies(currencies)
    }
}