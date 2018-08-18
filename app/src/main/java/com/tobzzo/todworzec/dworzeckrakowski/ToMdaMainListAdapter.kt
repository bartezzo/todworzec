package com.tobzzo.todworzec.dworzeckrakowski

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ToMdaMainListAdapter(internal var context: Context, internal var layoutResourceId: Int,
                           objects: List<ToMdaCity>?) : ArrayAdapter<ToMdaCity>(context, layoutResourceId, objects) {
    internal var objects: List<ToMdaCity>? = null

    init {
        this.objects = objects
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        var holder: ViewHolder? = null

        if (row == null) {
            val inflater = (context as Activity).layoutInflater
            row = inflater.inflate(layoutResourceId, parent, false)

            holder = ViewHolder()
            //			holder.icon = (ImageView) row.findViewById(R.id.icon);
            holder.city = row!!.findViewById(R.id.city) as TextView
            holder.extendInfo = row.findViewById(R.id.extend_info) as TextView
            row.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }

        val item = objects!![position]

        //		holder.icon.setImageResource(R.drawable.abc_ab_bottom_solid_dark_holo);
        holder.city!!.text = item.city
        holder.extendInfo!!.text = item.extendInfo

        return row
    }

    internal class ViewHolder {
        //		ImageView icon;
        var city: TextView? = null
        var extendInfo: TextView? = null
    }
}
