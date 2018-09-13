package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Worker
import com.example.mwt.livedata.setInt
import com.example.mwt.util.*
import java.util.*

class TrackerWorkerManager : Worker () {

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val preference: SharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val previousDate = preference.getString(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)


        if(calendar.time.toString() == previousDate){
            val numerator = preference.getInt(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR)
            preference.setInt(SHARED_PREFERENCE_NUMERATOR_DAILY, 0)
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, calendar.time.toString()).apply()

        } else {
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, calendar.time.toString()).apply()
        }



        return Result.SUCCESS
    }
}