package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mwt.db.MWTDatabase
import com.example.mwt.db.dateprogressdb.DateProgressEntity
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

        // This checks if the day has changed.
        if(previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && previousDate != currentDate){

            val numerator = preference.getFloat(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)

            preference.setFloat(SHARED_PREFERENCE_AMOUNT_DAILY, 0f)
            preference.setString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate)

            get<MWTDatabase>().let {
                it.dateProgressDao().save(DateProgressEntity(previousDate, numerator))
                it.dailyLogDao().dropTable()
            }

        } else {
            preference.setString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate)
        }

        // This checks if the week has changed.
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){

        }

        // This checks id the months has changed.
        if(extractMonthYear(currentDate) == extractMonthYear(previousDate)){

        }

        return Result.success()

    }

    private fun extractMonthYear(date: String): String{

        val parseDate = date.split("-")
        return "${parseDate[0]}-${parseDate[1]}"

    }
}