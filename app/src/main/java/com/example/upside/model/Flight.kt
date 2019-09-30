package com.example.upside.model

class Flight(val airline:String, val start:Position, val end:Position) {
    override fun toString(): String {
        return "Flight(airline='$airline', start=$start, end=$end)"
    }
}

class Position(val datetime:String, val airport:String) // best name i could think of
{
    override fun toString(): String {
        return "Position(datetime='$datetime', airport='$airport')"
    }
}


/*
"outbound": {
    "airline": "aa",
    "start": {
        "datetime": "2016-10-29T09:00:00Z",
        "airport": "UKB"
    },
    "end": {
        "datetime": "2016-10-29T20:49:00Z",
        "airport": "CNX"
    }
}
 */