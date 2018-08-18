package com.tobzzo.todworzec.dworzeckrakowski

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import com.tobzzo.todworzec.dworzeckrakowski.ToMdaTimeTableActivity.Companion.TIME_TABLE_CITY_COMUNE
import com.tobzzo.todworzec.dworzeckrakowski.ToMdaTimeTableActivity.Companion.TIME_TABLE_CITY_DISTRICT
import com.tobzzo.todworzec.dworzeckrakowski.ToMdaTimeTableActivity.Companion.TIME_TABLE_CITY_EXTEND_INFO
import com.tobzzo.todworzec.dworzeckrakowski.ToMdaTimeTableActivity.Companion.TIME_TABLE_CITY_ID
import com.tobzzo.todworzec.dworzeckrakowski.ToMdaTimeTableActivity.Companion.TIME_TABLE_CITY_NAME
import com.tobzzo.todworzec.dworzeckrakowski.ToMdaTimeTableActivity.Companion.TIME_TABLE_CITY_OUT_VERSION
import com.tobzzo.todworzec.dworzeckrakowski.ToMdaTimeTableActivity.Companion.TIME_TABLE_CITY_PROVINCE
import com.tobzzo.todworzec.dworzeckrakowski.tools.DelayedTextWatcher
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class ToMdaMainActivity : ToMdaBaseActivity(), NavigationView.OnNavigationItemSelectedListener, OnTaskCompletedInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)

//        val fab = findViewById(R.id.fab) as FloatingActionButton
//        fab.setOnClickListener(View.OnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        })

//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        val toggle = ActionBarDrawerToggle(
//                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.setDrawerListener(toggle)
//        toggle.syncState()

//        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
//        val drawer_layout = findViewById(R.id.drawer_layout) as DrawerLayout
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
            super.onBackPressed()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

//        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()

        if (!setElements())
            return;
        if (!setListeners())
            return;

        setLastCity();

    }

    private fun getLastCityListFromSharedPreferences(): MutableList<ToMdaCity>? {
        var result: MutableList<ToMdaCity>? = null
        try {
            val mPrefs = getPreferences(Context.MODE_PRIVATE)
            val gson = Gson()
            val json = mPrefs.getString("ToMdaTimeTableItemList", "")
//            val listType = object : TypeToken<ArrayList<ToMdaCity>>() {
//
//            }.getType()
//            val itemList = Gson().fromJson(json,
//                    listType)

            val listType = object : TypeToken<MutableList<ToMdaCity>>() {}.type
            val itemList = Gson().fromJson<MutableList<ToMdaCity>>(json, listType)


            result = itemList
        } catch (ex: Exception) {
            Logger.e(className, "getLastCityListFromSharedPreferences", ex)
        }

        return result
    }

    private fun setLastCity() {
        try {
            //if (main_city_et.getText().length() == 0) {
            val itemList = getLastCityListFromSharedPreferences()
            if (itemList != null) {
                setLastCityAdapters(itemList)
            }
            //}
        } catch (ex: Exception) {
            Logger.e(className, "setLastCity", ex)
        }

    }

    private fun setElements(): Boolean {
        var result = false
        try {
//            cityEt = findViewById(R.id.main_city_et) as EditText
//            cityLv = findViewById(R.id.main_city_lv) as ListView
//            cityLvEmptyItem = findViewById(R.id.main_city_lv_empty_list_item) as TextView
            main_city_lv.emptyView = main_city_lv_empty_list_item

            main_city_lv.setVisibility(View.GONE)
            main_city_lv_empty_list_item.setVisibility(View.GONE)

//            cityLastSearchedLv = findViewById(R.id.main_city_last_searched_lv) as ListView
            main_city_last_searched_lv.setEmptyView(main_city_last_searched_lv_empty_list_item)
//            loadingPb = findViewById(R.id.main_loading_pb) as ProgressBar
            main_loading_pb.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
//            cityEtSearchIv = findViewById(R.id.main_city_et_search_iv) as ImageView
//            cityEtClearIv = findViewById(R.id.main_city_et_clear_iv) as ImageView
//            cityInBt = findViewById(R.id.main_city_in_bt) as Button
//            cityOutBt = findViewById(R.id.main_city_out_bt) as Button
//            cityInOutGroup = findViewById(R.id.main_city_in_out_group) as RadioGroup
//            cityMainStationTv = findViewById(R.id.main_station_tv) as TextView
            main_station_tv.setText(ToMdaLogic.cityMainStationText)
            main_loading_pb.setVisibility(View.INVISIBLE)
            result = true
        } catch (ex: Exception) {
            Logger.e(className, "setElements", ex)
        }

        return result
    }

    private fun setListeners(): Boolean {
        var result = false
        try {
            main_city_in_bt.setOnClickListener(View.OnClickListener {
                /*
             * main_city_in_bt.setPadding(30, 30, 30, 30);
             * main_city_out_bt.setPadding(0, 0, 0, 0);
             */
            })

            main_city_out_bt.setOnClickListener(View.OnClickListener {
                /*
             * main_city_out_bt.setPadding(30, 30, 30, 30);
             * main_city_in_bt.setPadding(0, 0, 0, 0);
             */
            })

            main_city_et.addTextChangedListener(object : DelayedTextWatcher(2200) {

                override fun afterTextChangedDelayed(s: Editable) {
                    filterCitiesByPrefix()
                }
            })

            main_city_lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = main_city_lv
                        .getItemAtPosition(position) as ToMdaCity

                var list: MutableList<ToMdaCity>? = getLastCityListFromSharedPreferences()
                if (list == null) {
                    list = ArrayList()
                }

                list = addUniqueItemToList(list, item)
                if (list.size > ToMdaLogic.ITEM_LIST_MAX_COUNT)
                    list.removeAt(list.size - 1)

                val mPrefs = getPreferences(Context.MODE_PRIVATE)
                val prefsEditor = mPrefs.edit()
                val gson = Gson()
                val json = gson.toJson(list)
                prefsEditor.putString("ToMdaTimeTableItemList", json)
                prefsEditor.apply()

                startItemActivity(item)
            }

            main_city_last_searched_lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = main_city_last_searched_lv
                        .getItemAtPosition(position) as ToMdaCity
                startItemActivity(item)
            }

            main_city_et_search_iv.setOnClickListener(View.OnClickListener { filterCitiesByPrefix() })
            main_city_et_clear_iv.setOnClickListener(View.OnClickListener {
                if (main_city_et != null)
                    main_city_et.setText("")

                main_city_lv.setVisibility(View.GONE)
                main_city_lv_empty_list_item.setVisibility(View.GONE)
                //                    main_city_last_searched_lv.setVisibility(View.VISIBLE);
                setLastCity()
            })

            main_station_tv.setOnClickListener(View.OnClickListener {
                when (ToMdaLogic.MSTART_CITY) {
                    ToMdaLogic.MSTART_CITY_KRAKOW -> {
                        ToMdaLogic.MSTART_CITY = ToMdaLogic.MSTART_CITY_NOWY_SACZ
                        main_station_tv.setText("Nowy Sącz")
                    }
                    ToMdaLogic.MSTART_CITY_NOWY_SACZ -> {
                        ToMdaLogic.MSTART_CITY = ToMdaLogic.MSTART_CITY_KRAKOW
                        main_station_tv.setText("Kraków")
                    }
                    else -> {
                    }
                }
            })

            result = true
        } catch (ex: Exception) {
            Logger.e(className, "setListeners", ex)
        }

        return result
    }

    private fun filterCitiesByPrefix() {
        // String cities = parseHtml(s.toString());
        // cityTv.setText(cities);
        val prefix = getPrefix()
        if ("" != prefix) {
            ToMdaLogic.reloadTimeTable(getPrefix(),
                    this@ToMdaMainActivity)
        }
    }

    private fun startItemActivity(item: ToMdaCity) {
        try {
            val intent = createItemIntent(item)
            startActivity(intent)
        } catch (ex: Exception) {
            Logger.e(className, "startItemActivity", ex)
        }

    }

    private fun createItemIntent(item: ToMdaCity): Intent? {
        var intent: Intent? = null
        try {
            intent = Intent(this@ToMdaMainActivity,
                    ToMdaTimeTableActivity::class.java)

            //TODO pass only item object
            with(intent) {
                putExtra(TIME_TABLE_CITY_ID, item.id)
                putExtra(TIME_TABLE_CITY_NAME, item.city)
                putExtra(TIME_TABLE_CITY_COMUNE, item.commune)
                putExtra(TIME_TABLE_CITY_DISTRICT, item.district)
                putExtra(TIME_TABLE_CITY_PROVINCE, item.province)
                putExtra(TIME_TABLE_CITY_EXTEND_INFO, item.extendInfo)
                putExtra(TIME_TABLE_CITY_OUT_VERSION, isOutVersion())
            }
        } catch (ex: Exception) {
            Logger.e(className, "createItemIntent", ex)
        }

        return intent
    }

    private fun isOutVersion(): Boolean {
        try {
            val radioButtonID = main_city_in_out_group.checkedRadioButtonId
            val radioButton = main_city_in_out_group.findViewById<View>(radioButtonID)
//            val idx = main_city_in_out_group.indexOfChild(radioButton)
            if (radioButton == main_city_out_bt)
            return true
        } catch (e: Exception) {
            Logger.e(className, "isOutVersion error:" + e.message)
        }

        return false
    }

    private fun getPrefix(): String {
        try {
            var result = (if (main_city_et != null && main_city_et.getText().length > 1)
                main_city_et
                        .getText().toString()
            else
                "").toUpperCase()
            result = result.trim { it <= ' ' }
            return result
        } catch (e: Exception) {
            Logger.e(className, "getPrefix error:" + e.message)
        }

        return ""
    }

    private fun setLastCityAdapters(objects: List<ToMdaCity>) {
        try {
            val ad = ToMdaMainListAdapter(this,
                    R.layout.to_mda_main_list_adapter_item_row, objects)

            // View header = (View) getLayoutInflater().inflate(
            // R.layout.to_mda_main_list_adapter_item_header, null);
            // main_city_lv.addHeaderView(header);

            main_city_last_searched_lv.setAdapter(ad)

        } catch (ex: Exception) {
            Logger.e(className, "setLastCityAdapters", ex)
        }

    }

    private fun setAdapters() {
        try {
            val objects = ToMdaLogic.timeTablesOut
            objects?.let {
                val adapter = ToMdaMainListAdapter(this,
                        R.layout.to_mda_main_list_adapter_item_row, objects)

                // View header = (View) getLayoutInflater().inflate(
                // R.layout.to_mda_main_list_adapter_item_header, null);
                // main_city_lv.addHeaderView(header);
                //main_city_lv.setVisibility(View.VISIBLE);
                //            main_city_last_searched_lv.setVisibility(View.GONE);
                main_city_lv.adapter = adapter
            }

        } catch (ex: Exception) {
            Logger.e(className, "setAdapters", ex)
        }

    }

    override fun onTaskCompleted(outVersion: Boolean) {
        try {
            setAdapters()
            main_loading_pb.visibility = View.INVISIBLE

        } catch (e: Exception) {
            Logger.e(className, "onTaskCompleted error:" + e.message)
        }

    }

    override fun onTaskPreparing() {
        try {
            main_loading_pb.setVisibility(View.VISIBLE)
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

    private fun addUniqueItemToList(
            list: MutableList<ToMdaCity>, itemToAdd: ToMdaCity): MutableList<ToMdaCity> {
        for (item in list) {
            if (itemToAdd.city.equals(item.city, true))
                return list
        }

        list.add(0, itemToAdd)
        return list
    }
}
