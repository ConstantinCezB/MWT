package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mwt.db.MWTDatabase
import com.example.mwt.db.achievementdb.AchievementsEntity
import com.example.mwt.db.bmiRecordsdb.BMIRecordEntity
import com.example.mwt.db.dateprogressdb.DateProgressEntity
import com.example.mwt.util.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.util.*

class TrackerWorkerManager(context: Context, params: WorkerParameters) : Worker(context, params), KoinComponent {

    override fun doWork(): Result {

        val calendar = Calendar.getInstance()
        val preference: SharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)

        val previousDate: String = preference.getString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)!!
        val currentDate: String = calendar.getDate()

        // This checks if the day has changed.
        if (previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && previousDate != currentDate) {

            val dayAmount = preference.getFloat(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
            val dayGoal = preference.getFloat(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY)

            get<MWTDatabase>().let {
                if (dayAmount >= dayGoal) it.achievementsDao().save(AchievementsEntity("Reached Daily goal", "Day: $currentDate", "Day"))
                it.dateProgressDao().save(DateProgressEntity(previousDate, dayAmount))
                it.dailyLogDao().dropTable()
            }

            preference.setFloat(SHARED_PREFERENCE_AMOUNT_DAILY, 0f)
            preference.setString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate)

        } else {
            preference.setString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate)
        }

        // This checks if the week has changed.
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            val weekAmount = preference.getFloat(SHARED_PREFERENCE_AMOUNT_WEEKLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
            val weekGoal = preference.getFloat(SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY)

            if (weekAmount >= weekGoal) get<MWTDatabase>().let {
                it.achievementsDao().save(AchievementsEntity("Reached Weekly goal", "Week: $currentDate", "Week"))
                it.bmiRecordDao().save(BMIRecordEntity(200f, currentDate))
            }

            preference.setFloat(SHARED_PREFERENCE_AMOUNT_WEEKLY, 0f)
        }

        // This checks id the months has changed.
        if (extractMonthYear(currentDate) == extractMonthYear(previousDate)) {
            val monthAmount = preference.getFloat(SHARED_PREFERENCE_AMOUNT_MONTHLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
            val monthGoal = preference.getFloat(SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY)

            if (monthAmount >= monthGoal)
                get<MWTDatabase>().achievementsDao().save(AchievementsEntity("Reached Month goal", "Week: ${extractMonthYear(currentDate)}", "Month"))

            preference.setFloat(SHARED_PREFERENCE_AMOUNT_MONTHLY, 0f)
        }

        return Result.success()

    }

    private fun extractMonthYear(date: String): String {
        val parseDate = date.split("-")
        return "${parseDate[0]}-${parseDate[1]}"
    }
}