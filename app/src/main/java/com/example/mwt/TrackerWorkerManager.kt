package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
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

        if(previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && previousDate != currentDate){
            val numerator = preference.getFloat(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
            preference.setInt(SHARED_PREFERENCE_AMOUNT_DAILY, 0)
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate).apply()

            get<MWTDatabase>().let {
                it.dateProgressDao().save(DateProgressEntity(previousDate, numerator))
                it.dailyLogDao().dropTable()
            }

        } else {
            preference.edit().putString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate).apply()
        }

        return Result.success()
    }
}