package com.tobzzo.todworzec.dworzeckrakowski


fun ToMdaCitiesResponse.Locality.mapToCity(): ToMdaCity {
    return ToMdaCity(
            locality_id
            , locality_name
            , locality_province
            , locality_district
            , locality_commune
            , locality_country
    )
}


fun ToMdaCourseResponse.Course.mapToCourse(): ToMdaCourse {
    return ToMdaCourse(
            courseId
            , departure
            , arrival
            , durationMinutes
            , distance
            , price
            , priceVat
            , availablePlaces
            , carrier?.description ?: ToMdaLogic.EMPTY_TEXT
            , carrier?.vatNumber ?: ToMdaLogic.EMPTY_TEXT
            , localityFrom
            , localityTo
            , stationFrom
            , stationFromId
            , stationTo
            , stationToId
            , courseType
            , courseBeginStation
            , courseEndStation
            , withDiscount
            , beginStationLocality
            , endStationLocality
            , saleClosed
            , arrivalTime
            , arrivalDate
            , departureTime
            , departureDate
            , interval
    )
}

