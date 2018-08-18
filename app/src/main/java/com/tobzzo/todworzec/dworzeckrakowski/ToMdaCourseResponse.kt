package com.tobzzo.todworzec.dworzeckrakowski

import com.google.gson.annotations.SerializedName

class ToMdaCourseResponse {
    @SerializedName("status")
    private val status: ToMdaHttpStatusCode? = null

    @SerializedName("contents")
    private val contents: Contents? = null

    val responseCode : String?
    get() = status?.httpCode

    val courses: Array<Course>
        get() = contents?.table ?: emptyArray()


    class Contents {
        @SerializedName("table")
        var table: Array<Course>? = null
    }

    class Course {
        @SerializedName("course_id")
        var courseId: String? = null

        @SerializedName("departure")
        var departure: String? = null

        @SerializedName("arrival")
        var arrival: String? = null

        @SerializedName("duration_minutes")
        var durationMinutes: String? = null

        @SerializedName("distance")
        var distance: String? = null

        @SerializedName("price")
        var price: String? = null

        @SerializedName("price_vat")
        var priceVat: String? = null

        @SerializedName("available_places")
        var availablePlaces: String? = null

        @SerializedName("carrier")
        var carrier: CourseCarrier? = null

        @SerializedName("locality_from")
        var localityFrom: String? = null

        @SerializedName("locality_to")
        var localityTo: String? = null

        @SerializedName("station_from")
        var stationFrom: String? = null

        @SerializedName("station_from_id")
        var stationFromId: String? = null

        @SerializedName("station_to")
        var stationTo: String? = null

        @SerializedName("station_to_id")
        var stationToId: String? = null

        @SerializedName("course_type")
        var courseType: String? = null

        @SerializedName("course_begin_station")
        var courseBeginStation: String? = null

        @SerializedName("course_end_station")
        var courseEndStation: String? = null

        @SerializedName("with_discount")
        var withDiscount: String? = null

        @SerializedName("begin_station_locality")
        var beginStationLocality: String? = null

        @SerializedName("end_station_locality")
        var endStationLocality: String? = null

        @SerializedName("sale_closed")
        var saleClosed: String? = null

        @SerializedName("arrival_time")
        var arrivalTime: String? = null

        @SerializedName("arrival_date")
        var arrivalDate: String? = null

        @SerializedName("departure_time")
        var departureTime: String? = null

        @SerializedName("departure_date")
        var departureDate: String? = null

        @SerializedName("interval")
        var interval: String? = null
    }

    class CourseCarrier {
        @SerializedName("description")
        var description: String? = null

        @SerializedName("vat_number")
        var vatNumber: String? = null
    }
}

