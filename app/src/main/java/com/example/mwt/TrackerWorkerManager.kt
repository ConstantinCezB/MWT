package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mwt.db.MWTDatabase
import com.example.mwt.db.dateprogressdb.DateProgressEntity
import com.example.mwt.util.setInt
import com.example.mwt.util.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.util.*

class TrackerWorkerManager(context : Context, params : WorkerParameters) : Worker (context, params), KoinComponent {

    override fun doWork(): Result {

        val calendar = Calendar.getInstance()
        val preference: SharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val previousDate: String = preference.getString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)!!
        val currentDate: String = calendar.getDate()

        Log.d("TEST", currentDate)

        if(previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && previousDate != currentDate){
            val numerator = preference.getInt(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR)
            preference.setInt(SHARED_PREFERENCE_NUMERATOR_DAILY, 0)
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate).apply()
            get<MWTDatabase>().dateProgressDao().save(DateProgressEntity(previousDate, numerator))

        } else {
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate).apply()
        }

        return Result.success()
    }
}