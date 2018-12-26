package com.example.mwt.util

import java.util.*

fun Calendar.getDate() : String {

    val month = this.get(Calendar.MONTH)
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

    return "$hour:$minuteText:$secondText$periodText   ${this.getDate()}"
}