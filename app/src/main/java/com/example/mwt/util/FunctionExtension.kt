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