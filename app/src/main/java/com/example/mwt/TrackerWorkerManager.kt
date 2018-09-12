package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Worker
import com.example.mwt.livedata.setInt
import com.example.mwt.util.*
import java.util.*

class TrackerWorkerManager() : Worker () {

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val preference: SharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        Log.d("HELP13", "WORKS BABY1")

        if(calendar.get(Calendar.HOUR) == 0 && calendar.get(Calendar.MINUTE) == 0){
            val numerator = preference.getInt(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR)
            preference.setInt(SHARED_PREFERENCE_NUMERATOR_DAILY, 0)
            Log.d("HELP13", "WORKS BABY2")
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

        }
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {

        }

        return Result.SUCCESS
    }
}