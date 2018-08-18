package com.tobzzo.todworzec.dworzeckrakowski

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.orhanobut.logger.Logger
import java.util.*

class ToMdaCityTimeTableListAdapter(internal var context: Context, internal var layoutResourceId: Int,
                                    objects: List<ToMdaCourse>?, internal var outVersion: Boolean) : ArrayAdapter<ToMdaCourse>(context, layoutResourceId, objects) {
    internal var objects: List<ToMdaCourse>? = null

    init {
        this.objects = objects
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var row = convertView
        var holder: ViewHolder? = null

        when (row) {
            null -> {
                val inflater = (context as Activity).layoutInflater
                row = inflater.inflate(layoutResourceId, parent, false)

                holder = ViewHolder().apply {
                    company = row.findViewById(R.id.company) as TextView
                    departureTime = row
                            .findViewById(R.id.departureTime) as TextView
                    arrivalTime = row.findViewById(R.id.arrivalTime) as TextView
                    travelTime = row.findViewById(R.id.travelTime) as TextView
                    ticketPrice = row.findViewById(R.id.ticketPrice) as TextView
                    ticketsAvailableNumber = row
                            .findViewById(R.id.ticketsAvailableNumber) as TextView

                    infoLayout = row.findViewById(R.id.info_layout) as LinearLayout
                    timer = row.findViewById(R.id.timer) as TextView
                    timerUnit = row.findViewById(R.id.timer_unit) as TextView
                    timerImage = row
                            .findViewById(R.id.timer_clock_iv) as ImageView

                    infoLayoutInKrk = row.findViewById(R.id.info_layout_in_krk) as LinearLayout
                    timerInKrk = row.findViewById(R.id.timer_in_krk) as TextView
                    timerUnitInKrk = row.findViewById(R.id.timer_unit_in_krk) as TextView
                    timerImageInKrk = row
                            .findViewById(R.id.timer_clock_iv_in_krk) as ImageView


                    row.tag = this
                }
            }
            else -> holder = row.tag as ViewHolder
        }

        val item = objects?.get(position)
        item?.let {
            val color = context.resources.getColor(
                    calculateTimerTextColor(item.departure.toDate()))
            val colorInKrk = context.resources.getColor(
                    calculateTimerTextColor(item.arrival.toDate()))
            with(holder) {
                company?.text = item.carrier_description
                departureTime?.text = item.departure_time
                arrivalTime?.text = item.arrival_time
                travelTime?.text = formatTravelTime(item.duration_minutes)
                ticketPrice?.text = item.price
                ticketsAvailableNumber?.text = item.available_places

                timer?.text = calculateTimer(item.departure.toDate())
                timer?.setTextColor(color)
                timerUnit?.text = calculateTimerUnit(item.departure.toDate())
                timerUnit?.setTextColor(color)
                timerImage?.setColorFilter(color)
//
                timerInKrk?.text = calculateTimer(item.arrival.toDate())
                timerInKrk?.setTextColor(color)
                timerUnitInKrk?.text = calculateTimerUnit(item.arrival.toDate())
                timerUnitInKrk?.setTextColor(color)
                timerImageInKrk?.setColorFilter(color)
//
                //TODO remove ?
                infoLayout?.visibility = outVersion.toVisibility();
                infoLayoutInKrk?.visibility = (!outVersion).toVisibility();
            }
        }
        return row
    }

    private fun formatTravelTime(travelTime: String?): String? {
        var result = travelTime
        try {
            travelTime?.let {
                val minutes = it.toInt()
                val hours = minutes / 60
                val minutesInHour = minutes % 60
                result = when {
                    minutes <= 0 -> ".."
                    minutes in 1..59 -> "${minutes}m"
                    else -> "${hours}h${minutesInHour}m"
                }
            }
        } catch (ex: Exception) {
            Logger.e("ToMdaCityTimeTableListAdapter", "formatTravelTime", ex)
        }

        return result
    }

    private fun calculateTimer(departureDate: Date?): String {
        var result = "--"
        try {
            val nowCalendar = Calendar.getInstance()
            val nowDate = nowCalendar.time

            val millis = departureDate!!.time - nowDate.time

            // String timer = String.format(
            // "%d min, %d sec",
            // TimeUnit.MILLISECONDS.toMinutes(millis),
            // TimeUnit.MILLISECONDS.toSeconds(millis)
            // - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
            // .toMinutes(millis)));

            val seconds = (millis / 1000)
            val secondsInMinute = (millis / 1000) % 60
            val minutes = (millis / (1000 * 60))
            val minutesInHour = (millis / (1000 * 60) % 60)
            val hoursInDay = (millis / (1000 * 60 * 60) % 24)
            val hours = (millis / (1000 * 60 * 60))
            val days = (millis / (1000 * 60 * 60 * 24))

            when {
                days > 1L -> result = String.format("%d", days)

                days == 1L -> result = when {
                    hoursInDay > 12 -> String.format("%d,5", days,
                            hoursInDay)
                    else -> String.format("%d", days, hoursInDay)
                }

                hours > 1 -> result = String.format("%d", hours)

                hours == 1L -> result = when {
                    minutesInHour > 30 -> String.format("%d,5", hours,
                            minutesInHour)
                    else -> String.format("%d", hours,
                            minutesInHour)
                }

                minutes > 1L -> result = String.format("%d", minutes)

                minutes == 1L -> result = when {
                    secondsInMinute > 30 -> String.format("%d,5", minutes,
                            secondsInMinute)
                    else -> String.format("%d", minutes,
                            secondsInMinute)
                }

                seconds > 0 -> result = String.format("%d", seconds)
                else -> result = "---"
            }

            // {
            // if(minutes > 60)
            // result = String.format("%dh %dm", hours, minutesInHour);
            // else
            // if(seconds > 60)
            // result = String.format("%dm", minutes);
            // else
            // result = String.format("%dm", seconds);
            // }

        } catch (ex: Exception) {
            Logger.e("ToMdaCityTimeTableListAdapter", "calculateTimer", ex)
        }

        return result
    }

    private fun calculateTimerUnit(departureDate: Date?): String {
        var result = "m"
        try {
            val nowCalendar = Calendar.getInstance()
            val nowDate = nowCalendar.time

            val millis : Long = departureDate?.let { departureDate.time - nowDate.time } ?: run { 0L }

            val seconds = (millis / 1000)
            val secondsInMinute = (millis / 1000) % 60
            val minutes = (millis / (1000 * 60))
            val minutesInHour = (millis / (1000 * 60) % 60)
            val hoursInDay = (millis / (1000 * 60 * 60) % 24)
            val hours = (millis / (1000 * 60 * 60))
            val days = (millis / (1000 * 60 * 60 * 24))

            result = when {
                days >= 1 -> "d"
                minutes > 60 -> "h"
                seconds > 0 -> "m"
                else -> ""
            }
        } catch (ex: Exception) {
            Logger.e("ToMdaCityTimeTableListAdapter", "calculateTimer", ex)
        }

        return result
    }

    private fun calculateTimerTextColor(departureDate: Date?): Int {
        var result = 0
        try {
            val nowCalendar = Calendar.getInstance()
            val nowDate = nowCalendar.time

            val millis = departureDate!!.time - nowDate.time

            val seconds = (millis / 1000).toLong()
            val secondsInMinute = (millis / 1000).toLong() % 60
            val minutes = (millis / (1000 * 60)).toLong()
            val minutesInHour = (millis / (1000 * 60) % 60).toLong()
            val hoursInDay = (millis / (1000 * 60 * 60) % 24).toLong()
            val hours = (millis / (1000 * 60 * 60)).toLong()
            val days = (millis / (1000 * 60 * 60 * 24)).toLong()

            result = when {
                days >= 1 -> R.color.timer_color_01_00_00
                hours >= 1 -> R.color.timer_color_00_01_00
                minutes >= 15 -> R.color.timer_color_00_00_15
                minutes >= 5 -> R.color.timer_color_00_00_05
                seconds > 0 -> R.color.timer_color_00_00_00
                else -> R.color.timer_color_minus
            }

        } catch (ex: Exception) {
            Logger.e("ToMdaCityTimeTableListAdapter", "calculateTimer", ex)
        }

        return result
    }

    internal class ViewHolder {
        var company: TextView? = null
        var destinationCity: TextView? = null
        var departureTime: TextView? = null
        var arrivalTime: TextView? = null
        var travelTime: TextView? = null
        var ticketPrice: TextView? = null
        var ticketsAvailableNumber: TextView? = null

        var infoLayout: LinearLayout? = null
        var timer: TextView? = null
        var timerUnit: TextView? = null
        var timerImage: ImageView? = null

        var infoLayoutInKrk: LinearLayout? = null
        var timerInKrk: TextView? = null
        var timerUnitInKrk: TextView? = null
        var timerImageInKrk: ImageView? = null
    }
}

//TODO to extension class
private fun Boolean.toVisibility(): Int {
    return this.let { if (this) View.VISIBLE else View.GONE }
}

//TODO to extension class
private fun String?.toDate(): Date? {
    return this?.let { ToMdaLogic.dateTimeFormat.parse(this) }
}
