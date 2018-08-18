package com.tobzzo.todworzec.dworzeckrakowski

import android.os.AsyncTask
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

//TODO object->class
object ToMdaLogic {
    //TODO: zmienic na static ToMdaMainActivity.MSTART_CITY {
    //TODO companion

    const val MSTART_CITY_KRAKOW = "krakow"
    const val MSTART_CITY_NOWY_SACZ = "nowy_sacz"
    var MSTART_CITY = MSTART_CITY_KRAKOW
    const val EMPTY_TEXT = "---"
    const val HTTP_REQUEST_TIMEOUT = 20 * 1000
    const val ITEM_LIST_MAX_COUNT = 5

    var timeTablesOut: List<ToMdaCity>? = null
        private set
    var cityTimeTables: List<ToMdaCourse>? = null
        private set

    val dateOnlyFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val timeOnlyFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateTimeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())

    private const val linkCities = "http://rozklady.mda.malopolska.pl/prm.php?url=GetLocalityid&locality_name=%s&search_type=1"
    private const val linkCityTimeTable = "http://rozklady.mda.malopolska.pl/prm.php?url=GetLocalityOrStationConnectionDetail&station_or_locality_from_id=%s&station_or_locality_to_id=%s&is_station_from=0&is_station_to=0&data=%s&czas=%s"

    private fun encode(valueToEncode: String?): String? {
        var result = valueToEncode
        try {
            result = URLEncoder.encode(valueToEncode, "UTF-8")
        } catch (e: Exception) {
            Logger.e("Error on encoding", e);
        }

        return result
    }

    private fun getClassName() = "ToMdaLogic"

    private fun cleanHtmlResponseBody(responseBody: String): String {
        var responseBody = responseBody
        try {
            responseBody = responseBody
                    .replace("\\u0104", "Ą")
                    .replace("\\u0105", "ą")
                    .replace("\\u0106", "Ć")
                    .replace("\\u0107", "ć")
                    .replace("\\u0118", "Ę")
                    .replace("\\u0119", "ę")
                    .replace("\\u0141", "Ł")
                    .replace("\\u0142", "ł")
                    .replace("\\u0143", "Ń")
                    .replace("\\u0144", "ń")
                    .replace("\\u00d3", "Ó")
                    .replace("\\u00f3", "ó")
                    .replace("\\u015a", "Ś")
                    .replace("\\u015b", "ś")
                    .replace("\\u0179", "Ź")
                    .replace("\\u017a", "ź")
                    .replace("\\u017b", "Ż")
                    .replace("\\u017c", "ż")
        } catch (ex: Exception) {
            Logger.e(getClassName(), "cleanHtmlResponseBody", ex)
        }

        return responseBody
    }

    fun reloadTimeTable(destinationPrefix: String,
                        listener: OnTaskCompletedInterface) {
        try {

            timeTablesOut = ArrayList()

            val loaderOut = ToMdaTimeTableLoader(listener)
            loaderOut.execute(timeTablesOut, Direction.OUT, destinationPrefix)

            listener.hideKeyboard()
        } catch (e: Exception) {
            Logger.e(getClassName(), "reloadTimeTable error: " + e.message)
        }
    }


    private class ToMdaCourseTableLoader(private val listener: OnTaskCompletedInterface) : AsyncTask<Any, Long, List<ToMdaCourse>>() {
        private var outVersion: Boolean = true

        override fun doInBackground(vararg params: Any): List<ToMdaCourse>? {
            try {
                val cityTimeTables = params[0] as ArrayList<ToMdaCourse>
                val cityItem = params[1] as ToMdaCity
                val date = params[2] as Pair<String, String>
                this.outVersion = (params[3] as Boolean)

                val urlWithParameters =
                        String.format(linkCityTimeTable
                                , getStartCity(outVersion, cityItem.id)
                                , getEndCity(outVersion, cityItem.id)
                                , date.first
                                , date.second)

                val doc2 = Jsoup
                        .connect(urlWithParameters)
                        .timeout(HTTP_REQUEST_TIMEOUT).ignoreContentType(true)
                val doc2get = doc2.get()

                var responseBody = doc2get.body().text().toString()
                responseBody = cleanHtmlResponseBody(responseBody)

                //TODO dagger
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                try {
                    val toMdaCourseResponse = gson.fromJson(responseBody, ToMdaCourseResponse::class.java)

                    Logger.d("Course response code:%s", toMdaCourseResponse.responseCode)
                    //TODO course mapTO item
                    for (courseResponse in toMdaCourseResponse.courses) {
                        cityTimeTables.add(courseResponse.mapToCourse())
                    }
                } catch (ex: Exception){
                    Logger.e("Course response error:%s", ex)
                }

                return cityTimeTables
            } catch (e: Exception) {
                Logger.e(getClassName(), "doInBackground error:" + e.message)
            }

            return cityTimeTables
        }

        override fun onPreExecute() {
            try {
                super.onPreExecute()
                listener.onTaskPreparing()
            } catch (e: Exception) {
                Logger.e(getClassName(), "onPreExecute error:" + e.message)
            }

        }

        override fun onPostExecute(result: List<ToMdaCourse>) {
            try {
                super.onPostExecute(result)

                listener.onTaskCompleted(this.outVersion)
            } catch (e: Exception) {
                Logger.e(getClassName(), "onPostExecute error:" + e.message)
            }
        }
    }

    private fun getStartCity(outVersion: Boolean, id: String?): String =
            if (outVersion) MSTART_CITY_KRAKOW else id ?: ""

    private fun getEndCity(outVersion: Boolean, id: String?): String =
            if (outVersion) id ?: "" else MSTART_CITY_KRAKOW


    private class ToMdaTimeTableLoader(private val listener: OnTaskCompletedInterface) : AsyncTask<Any, Long, List<ToMdaCity>>() {

        override fun doInBackground(vararg params: Any): List<ToMdaCity>? {
            var timeTables = emptyList<ToMdaCity>()
            try {
                timeTables = params[0] as ArrayList<ToMdaCity>
                var destinationPrefix = params[2] as String?
                destinationPrefix = encode(destinationPrefix)
                val url = String.format(linkCities, destinationPrefix)
                val doc2 = Jsoup
                        .connect(url)
                        .ignoreContentType(true)
                        .timeout(HTTP_REQUEST_TIMEOUT).get()

                var responseBody = doc2.body().text().toString()
                responseBody = cleanHtmlResponseBody(responseBody)

                //TODO dagger
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                val toMdaCitiesResponse = gson.fromJson(responseBody, ToMdaCitiesResponse::class.java)

                for (cityResponse in toMdaCitiesResponse.cities) {
                    timeTables.add(cityResponse.mapToCity())
                }

                return timeTables
            } catch (e: Exception) {
                Logger.e(getClassName(), "doInBackground error:" + e.message)
            }

            return timeTables
        }

        override fun onPreExecute() {
            try {
                super.onPreExecute()
                listener.onTaskPreparing()
            } catch (e: Exception) {
                Logger.e(getClassName(), "onPreExecute error:" + e.message)
            }
        }

        override fun onPostExecute(result: List<ToMdaCity>) {
            try {
                super.onPostExecute(result)

                listener.onTaskCompleted(true)
            } catch (e: Exception) {
                Logger.e(getClassName(), "onPostExecute error:" + e.message)
            }
        }
    }

    fun reloadCityTimeTable(item: ToMdaCity,
                            listener: OnTaskCompletedInterface, date: Pair<String, String>, outVersion: Boolean?) {
        try {
            cityTimeTables = ArrayList()
            val loaderOut = ToMdaCourseTableLoader(
                    listener)
            loaderOut.execute(cityTimeTables, item, date, outVersion)
            listener.hideKeyboard()
        } catch (e: Exception) {
            Logger.e(getClassName(), "reloadCityTimeTable error: " + e.message)
        }
    }

    val cityMainStationText: String
        get() {
            var result = "Kraków"
            try {
                when (MSTART_CITY) {
                    MSTART_CITY_KRAKOW -> {
                        result = "Kraków"
                    }
                    MSTART_CITY_NOWY_SACZ -> {
                        result = "Nowy Sącz"
                    }
                    else -> {
                    }
                }
            } catch (e: Exception) {
                Logger.e(getClassName(), "getCityMainStationText error: " + e.message)
            }
            return result
        }

}