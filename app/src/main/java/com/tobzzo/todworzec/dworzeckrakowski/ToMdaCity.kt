package com.tobzzo.todworzec.dworzeckrakowski

data class ToMdaCity(
        val id: String?
        , val city: String?
        , val province: String?
        , val district: String?
        , val commune: String?
        , val country: String?
) {
    val extendInfo: String
        get() = "$province / $district / $commune"
}
