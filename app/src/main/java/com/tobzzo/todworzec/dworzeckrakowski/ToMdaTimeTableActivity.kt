package com.tobzzo.todworzec.dworzeckrakowski

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.to_mda_time_table_activity.*
import java.util.*

class ToMdaTimeTableActivity : ToMdaBaseActivity(), OnTaskCompletedInterface {

    // private TextView cityId = null;
    private var cityName: TextView? = null
    private var cityExtendInfo: TextView? = null
    private var cityTimeTableLv: ListView? = null
    //    private TextView cityTimeTableLvEmptyItem = null;
    private var cityTimeTableLoadingPb: ProgressBar? = null
    private var cityMainStationTv: TextView? = null
    private var cityTimeTablePrevDayBt: ImageView? = null
    private var cityTimeTableNextDayBt: ImageView? = null
    private var cityTimeTableCurrentDayBt: ImageView? = null
    //	private TextView cityTimeTableCurrentDayTv = null;
    //	private TextView cityTimeTablePrevDayTv = null;
    //	private TextView cityTimeTableNextDayTv = null;
    private var cityTimeTableListDayTv: TextView? = null
    private var cityTimeTableOutBt: RadioButton? = null
    private var cityTimeTableInBt: RadioButton? = null
    private var cityInOutGroup: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.to_mda_time_table_activity)

        setElements()
    }


    private fun loadElementsValues(outVersion: Boolean?): Boolean {
        var result = false
        try {
            cityMainStationTv!!.text = ToMdaLogic.cityMainStationText

            if (intent != null) {
                val cityIdValue = intent.getStringExtra(TIME_TABLE_CITY_ID)
                val cityNameValue = intent
                        .getStringExtra(TIME_TABLE_CITY_NAME)

                val cityProvinceValue = intent
                        .getStringExtra(TIME_TABLE_CITY_PROVINCE)

                val cityDistrictValue = intent
                        .getStringExtra(TIME_TABLE_CITY_DISTRICT)

                val cityComuneValue = intent
                        .getStringExtra(TIME_TABLE_CITY_COMUNE)

                val cityExtendInfoValue = intent
                        .getStringExtra(TIME_TABLE_CITY_EXTEND_INFO)

                val date = intent.getLongExtra(TIME_TABLE_CITY_DATE, defaultDate)


                (findViewById<RadioButton>(R.id.time_table_city_out_bt)).isChecked = outVersion!!
                (findViewById<RadioButton>(R.id.time_table_city_in_bt)).isChecked = !outVersion
                //
                // if (cityId != null)
                // cityId.setText(cityIdValue);
                if (cityName != null)
                    cityName!!.text = cityNameValue
                if (cityExtendInfo != null)
                    cityExtendInfo!!.text = cityExtendInfoValue

                val cityItem = ToMdaCity(
                        cityIdValue, cityNameValue, cityProvinceValue,
                        cityDistrictValue, cityComuneValue, "PL")

                val dayTimeSplitted = calculateDay(date)
                ToMdaLogic.reloadCityTimeTable(cityItem, this@ToMdaTimeTableActivity, dayTimeSplitted, outVersion)

                cityTimeTableListDayTv!!.text = dayTimeSplitted.first

                cityTimeTableOutBt!!.isChecked = outVersion
                cityTimeTableInBt!!.isChecked = !outVersion
            }

            result = true
        } catch (ex: Exception) {
            Logger.e(className, "loadElementsValues", ex)
        }

        return result
    }

    private fun setElements(): Boolean {
        var result = false
        try {
            //TODO kotterknife
//             cityId = (TextView) findViewById(R.id.time_table_city_id_tv);
            cityName = findViewById<TextView>(R.id.time_table_city_name_tv)
            cityExtendInfo = findViewById<TextView>(R.id.time_table_city_extend_info_tv)
            cityTimeTableLv = findViewById<ListView>(R.id.city_time_table_lv)
            (cityTimeTableLv as ListView).emptyView = findViewById(R.id.city_time_table_lv_empty_list_item)
            //            cityTimeTableLvEmptyItem = SS(TextView)findViewById(R.id.city_time_table_lv_empty_list_item);
            //            cityTimeTableLvEmptyItem.setVisibility(View.GONE);
            cityTimeTableLoadingPb = findViewById<ProgressBar>(R.id.city_time_table_loading_pb)
            cityMainStationTv = findViewById<TextView>(R.id.city_time_table_station_tv)
            cityTimeTableLoadingPb!!.progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            cityTimeTablePrevDayBt = findViewById<ImageView>(R.id.time_table_city_prev_day_bt)
            cityTimeTableNextDayBt = findViewById<ImageView>(R.id.time_table_city_next_day_bt)
            cityTimeTableCurrentDayBt = findViewById<ImageView>(R.id.time_table_city_current_day_bt)
            //			cityTimeTableCurrentDayTv = (TextView) findViewById(R.id.time_table_city_current_day_tv);
            //			cityTimeTablePrevDayTv = (TextView) findViewById(R.id.time_table_city_prev_day_tv);
            //			cityTimeTableNextDayTv = (TextView) findViewById(R.id.time_table_city_next_day_tv);
            cityTimeTableListDayTv = findViewById<TextView>(R.id.time_table_city_list_day_tv)
            cityTimeTableInBt = findViewById<RadioButton>(R.id.city_time_table_in_bt)
            cityTimeTableOutBt = findViewById<RadioButton>(R.id.city_time_table_out_bt)
            cityInOutGroup = findViewById<RadioGroup>(R.id.city_time_table_in_out_group)

            val outVersionFromIntent = intent.getBooleanExtra(TIME_TABLE_CITY_OUT_VERSION, true)
            loadElementsValues(outVersionFromIntent)
            setLElementsListeners()
            result = true
        } catch (ex: Exception) {
            Logger.e(className, "setElements", ex)
        }

        return result
    }

    private fun setAdapters(outVersion: Boolean) {
        try {

            val objects = ToMdaLogic
                    .cityTimeTables
            // ToMdaMainListAdapter ad = new ToMdaMainListAdapter(this,
            // R.layout.to_mda_main_list_adapter_item_row, objects);

            val ad = ToMdaCityTimeTableListAdapter(
                    this,
                    R.layout.to_mda_city_time_table_list_adapter_item_row,
                    objects, outVersion
            )
            cityTimeTableLv!!.adapter = ad


        } catch (ex: Exception) {
            Logger.e(className, "setAdapters", ex)
        }

    }

    private fun getDateFromString(dateInput: String): Date {
        var result = Calendar.getInstance().time
        try {
            try {
                val date = ToMdaLogic.dateOnlyFormat.parse(dateInput)
                result = date
            } catch (e: Exception) {
                Logger.e(className, "getDate error:" + e.message)
            }

            return result

        } catch (ex: Exception) {
            Logger.e(className, "getDateFromString", ex)
        }

        return result
    }

    private fun calculateDay(dateInput: Long): Pair<String, String> {
        var resultDate = ""
        var resultTime = ""
        try {
            try {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = dateInput
                resultDate = ToMdaLogic.dateOnlyFormat.format(calendar.time)
                resultTime = ToMdaLogic.timeOnlyFormat.format(calendar.time).replace(":", "%3A", true)
            } catch (e: Exception) {
                Logger.e(className, "getDate error:" + e.message)
            }
        } catch (ex: Exception) {
            Logger.e(className, "calculateDay", ex)
        }

        return Pair(resultDate, resultTime)
    }

    private val defaultDate = Calendar.getInstance().timeInMillis

    private fun seekIntentDay(days: Int?) {
        try {
            with(intent) {

                val date = getLongExtra(TIME_TABLE_CITY_DATE, defaultDate)
                val calendar = Calendar.getInstance()
                days?.let {
                    calendar.timeInMillis = date
                    calendar.add(Calendar.DAY_OF_YEAR, days)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                }

                removeExtra(TIME_TABLE_CITY_DATE)
                putExtra(TIME_TABLE_CITY_DATE, calendar.timeInMillis)
                val outVersionFromIntent = intent.getBooleanExtra(TIME_TABLE_CITY_OUT_VERSION, true)
                loadElementsValues(outVersionFromIntent)
            }
        } catch (ex: Exception) {
            Logger.e(className, "seekIntentDay", ex)
        }

    }

    private fun setLElementsListeners() {
        try {
            cityTimeTableNextDayBt!!
                    .setOnClickListener { seekIntentDay(1) }

            cityTimeTablePrevDayBt!!
                    .setOnClickListener { seekIntentDay(-1) }
            cityTimeTableCurrentDayBt!!
                    .setOnClickListener { seekIntentDay(null) }
            cityTimeTableInBt!!.setOnClickListener { changeDirectionInAdapterToOut(false) }

            cityTimeTableOutBt!!.setOnClickListener { changeDirectionInAdapterToOut(true) }

            val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val dateFromIntentString = intent.getStringExtra(TIME_TABLE_CITY_DATE)
                val dateFromIntent = getDateFromString(dateFromIntentString)

                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)

                val diffInMilis = calendar.timeInMillis - dateFromIntent.time
                val diffInDays = Math.ceil((diffInMilis / 1000 / 60 / 60 / 24).toDouble()).toInt()
                seekIntentDay(diffInDays)
            }

            cityTimeTableListDayTv!!.setOnClickListener {
                val dateFromIntentString = intent.getStringExtra(TIME_TABLE_CITY_DATE)
                val dateFromIntent = getDateFromString(dateFromIntentString)
                val calendar = Calendar.getInstance()
                calendar.time = dateFromIntent

                DatePickerDialog(this@ToMdaTimeTableActivity, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        } catch (ex: Exception) {
            Logger.e(className, "setLElementsListeners", ex)
        }

    }

    private fun changeDirectionInAdapterToOut(outVersion: Boolean) {
        val oldIntent = intent
        val outVersionFromOldIntent = oldIntent.getBooleanExtra(TIME_TABLE_CITY_OUT_VERSION, true)

        oldIntent.removeExtra(ToMdaTimeTableActivity.TIME_TABLE_CITY_OUT_VERSION)
        oldIntent.putExtra(ToMdaTimeTableActivity.TIME_TABLE_CITY_OUT_VERSION, outVersion)

        loadElementsValues(outVersion)
    }

    override fun onTaskCompleted(outVersion: Boolean) {
        try {
            setAdapters(outVersion)
            cityTimeTableLoadingPb!!.visibility = View.INVISIBLE
            //            cityTimeTableListDayTv.setVisibility(View.VISIBLE);

        } catch (e: Exception) {
            Logger.e(className, "onTaskCompleted error:" + e.message)
        }

    }

    override fun onTaskPreparing() {
        try {
            cityTimeTableLoadingPb!!.visibility = View.VISIBLE
            //            cityTimeTableListDayTv.setVisibility(View.GONE);
        } catch (e: Exception) {
            Logger.e(className, "onTaskPreparing error:" + e.message)
        }

    }

    override fun hideKeyboard() {
        try {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputManager.hideSoftInputFromWindow(
                    if (null == currentFocus)
                        null
                    else
                        currentFocus!!
                                .windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            Logger.e(className, "hideKeyboard error:" + e.message)
        }

    }

    companion object {
        var TIME_TABLE_CITY_ID = "TIME_TABLE_CITY_ID"
        var TIME_TABLE_CITY_NAME = "TIME_TABLE_CITY_NAME"
        var TIME_TABLE_CITY_COMUNE = "TIME_TABLE_CITY_COMUNE"
        var TIME_TABLE_CITY_DISTRICT = "TIME_TABLE_CITY_DISTRICT"
        var TIME_TABLE_CITY_PROVINCE = "TIME_TABLE_CITY_PROVINCE"

        var TIME_TABLE_CITY_EXTEND_INFO = "TIME_TABLE_CITY_EXTEND_INFO"
        var TIME_TABLE_CITY_DATE = "TIME_TABLE_CITY_DATE"
        var TIME_TABLE_CITY_OUT_VERSION = "TIME_TABLE_CITY_OUT_VERSION"
    }
}
