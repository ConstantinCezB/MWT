package com.example.mwt.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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