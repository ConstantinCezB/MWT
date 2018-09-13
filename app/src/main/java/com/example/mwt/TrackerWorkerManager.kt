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
        val previousDate = preference.getString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)

        Log.d("TrackerWorkerManager", "UPDATE:  previousDate = " + previousDate!!.toString() + "       currentDate = " + calendar.time.toString())
        if(calendar.time.toString() == previousDate.toString()){
            Log.d("TrackerWorkerManager", "This works !!")
            val numerator = preference.getInt(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR)
            preference.setInt(SHARED_PREFERENCE_NUMERATOR_DAILY, 0)
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, calendar.time.toString()).apply()

        } else {
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, calendar.time.toString()).apply()
        }



        return Result.SUCCESS
    }
}