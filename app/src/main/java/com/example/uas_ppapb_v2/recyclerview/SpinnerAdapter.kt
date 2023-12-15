package com.example.uas_ppapb_v2.recyclerview

import android.content.Context
import android.widget.ArrayAdapter

class SpinnerAdapter(context: Context, resource: Int, private val items: List<String>) :
    ArrayAdapter<String>(context, resource, items) {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): String? {
        return items[position]
    }
}