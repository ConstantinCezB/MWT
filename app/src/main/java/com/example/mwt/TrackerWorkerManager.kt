package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import com.example.mwt.livedata.setInt
import com.example.mwt.util.*
import java.util.*

class TrackerWorkerManager : Worker () {

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val preference: SharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val previousDate: String = preference.getString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)!!

        if(previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && previousDate != calendar.getDate()){
            val numerator = preference.getInt(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR)
            preference.setInt(SHARED_PREFERENCE_NUMERATOR_DAILY, 0)
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, calendar.getDate()).apply()
        } else {
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, calendar.getDate()).apply()
        }

        return Result.SUCCESS
    }

    private fun Calendar.getDate() : String {

        val month = this.get(Calendar.MONTH)
        val day = this.get(Calendar.DAY_OF_MONTH)

        val yearText = this.get(Calendar.YEAR).toString()
        var monthText = month.toString()
        var dayText = day.toString()

        if (month < 10) monthText = "0$monthText"
        if(day < 10) dayText = "0$dayText"

        return "$dayText/$monthText/$yearText"
    }
}