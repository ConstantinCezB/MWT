package com.example.mwt.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxisFormatter(formatterList: ArrayList<String>) : ValueFormatter() {
    private val days = formatterList
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}