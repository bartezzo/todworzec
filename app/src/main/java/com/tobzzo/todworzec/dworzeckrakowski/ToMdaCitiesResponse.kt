package com.tobzzo.todworzec.dworzeckrakowski

import com.google.gson.annotations.SerializedName


class ToMdaCitiesResponse {

    @SerializedName("status")
    private val status: ToMdaHttpStatusCode? = null


    @SerializedName("contents")
    private val contents: Contents? = null


    val cities: Array<Locality>
        get() = contents?.table ?: emptyArray()


    class Contents {
        @SerializedName("table")
        var table: Array<Locality>? = null
    }

    class Locality {
        @SerializedName("locality_name")
        var locality_name: String? = null

        @SerializedName("locality_id")
        var locality_id: String? = null

        @SerializedName("locality_commune")
        var locality_commune: String? = null

        @SerializedName("locality_district")
        var locality_district: String? = null

        @SerializedName("locality_province")
        var locality_province: String? = null

        @SerializedName("locality_country")
        var locality_country: String? = null
    }
}