package com.example.mwt.util

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.mwt.R
import kotlinx.android.synthetic.main.bmi_fragment.view.*
import java.util.*

fun Calendar.getDate() : String {

    val month = this.get(Calendar.MONTH) + 1
    val day = this.get(Calendar.DAY_OF_MONTH)

    val yearText = this.get(Calendar.YEAR).toString()
    var monthText = month.toString()
    var dayText = day.toString()

    if (month < 10) monthText = "0$monthText"
    if(day < 10) dayText = "0$dayText"

    return "$dayText/$monthText/$yearText"
}

fun Calendar.getTimeAndDate() : String {

    val hour = this.get(Calendar.HOUR)
    val minute = this.get(Calendar.MINUTE)
    val second = this.get(Calendar.SECOND)
    val period = this.get(Calendar.AM_PM)

    var minuteText = minute.toString()
    var secondText = second.toString()
    var periodText = "AM"

    if (minute < 10) minuteText = "0$minuteText"
    if (second < 10) secondText = "0$secondText"
    if (period == 1) periodText = "PM"

    return "$hour:$minuteText:$secondText$periodText\n\n${this.getDate()}"
}

fun ViewGroup.inflate(
        @LayoutRes layoutId: Int,
        inflater: LayoutInflater = LayoutInflater.from(context),
        attachToRoot: Boolean = false
): View {
    return inflater.inflate(layoutId, this, attachToRoot)
}

fun ConstraintLayout.showContent(dropIcon: ImageView) {
    if(this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
        dropIcon.background = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down)
    } else {
        this.visibility = View.VISIBLE
        dropIcon.background = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_up)
    }
}

fun ConstraintLayout.showContent(dropIcon: Button) {
    if(this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
        dropIcon.background = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down)
    } else {
        this.visibility = View.VISIBLE
        dropIcon.background = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_up)
    }
}

fun Spinner.attachSinner(preference: SharedPreferences, initialPos: Int, spinnerArray: Int, preferenceSaveVal: String) {
    val adapter = ArrayAdapter.createFromResource(context!!, spinnerArray, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    this.adapter = adapter
    this.spinnerGenger.setSelection(initialPos)
    this.spinnerGenger.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            preference.setString(preferenceSaveVal, parent?.getItemAtPosition(position).toString())
        }
    }
}