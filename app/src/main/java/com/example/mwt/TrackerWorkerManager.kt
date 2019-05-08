package com.example.mwt

import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mwt.MWTApplication.Companion.CHANNEL_ACHIEVEMENT_ID
import com.example.mwt.MWTApplication.Companion.CHANNEL_INTAKE_WATER_ID
import com.example.mwt.MWTApplication.Companion.CHANNEL_RECORD_BMI_ID
import com.example.mwt.db.MWTDatabase
import com.example.mwt.db.achievementdb.AchievementsEntity
import com.example.mwt.db.bmiRecordsdb.BMIRecordEntity
import com.example.mwt.db.dateprogressdb.DateProgressEntity
import com.example.mwt.util.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.util.*

class TrackerWorkerManager(context: Context, params: WorkerParameters) : Worker(context, params), KoinComponent {

    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)


    override fun doWork(): Result {

        val calendar = Calendar.getInstance()
        val currentDate: String = calendar.getDate()
        val preference: SharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)

        val previousDate: String = preference.getString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)!!
        val allowWeekReset = preference.getBoolean(SHARED_PREFERENCE_ALLOW_WEEK_RESET, DEFAULT_ALLOW_WEEK_RESET)
        val allowNotification = preference.getBoolean(SHARED_PREFERENCE_NOTIFICATION, DEFAULT_NOTIFICATION)
        val allowReminderNotification = preference.getBoolean(SHARED_PREFERENCE_DRINKING_REMINDER, DEFAULT_DRINKING_REMINDER)
        val allowAchievementNotification = preference.getBoolean(SHARED_PREFERENCE_ACHIEVEMENT_NOTIFICATION, DEFAULT_ACHIEVEMENT_NOTIFICATION)
        val allowBMIRecordNotification = preference.getBoolean(SHARED_PREFERENCE_BMI_RECORD_NOTIFICATION, DEFAULT_BMI_RECORD_NOTIFICATION)
        val bmiRecordInterval = preference.getString(SHARED_PREFERENCE_TIME_INTERVAL_BMI_RECORD, DEFAULT_TIME_INTERVAL_BMI_RECORD)
        val dayAmount = preference.getFloat(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
        val dayGoal = preference.getFloat(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY)
        val weekAmount = preference.getFloat(SHARED_PREFERENCE_AMOUNT_WEEKLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
        val weekGoal = preference.getFloat(SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY)
        val monthAmount = preference.getFloat(SHARED_PREFERENCE_AMOUNT_MONTHLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
        val monthGoal = preference.getFloat(SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY)


        sendOnNotificationIntake(allowNotification, allowReminderNotification)

        //==========================================================================================
        // This checks if the day has changed.
        if (previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && previousDate != currentDate) {
            get<MWTDatabase>().let {
                if (dayAmount >= dayGoal) achievementLogic(it, currentDate, allowNotification, allowAchievementNotification,"Day")
                it.dateProgressDao().save(DateProgressEntity(previousDate, dayAmount))
                it.dailyLogDao().dropTable()
                if (bmiRecordInterval == "day") bmiRecordLogic(it, currentDate, allowNotification, allowBMIRecordNotification)
            }
            preference.setFloat(SHARED_PREFERENCE_AMOUNT_DAILY, 0f)
        }

        //==========================================================================================
        // This checks if the week has changed.

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && allowWeekReset) {
            get<MWTDatabase>().let {
                if (weekAmount >= weekGoal) achievementLogic(it, currentDate, allowNotification, allowAchievementNotification, "Week")
                if (bmiRecordInterval == "week") bmiRecordLogic(it, currentDate, allowNotification, allowBMIRecordNotification)
            }
            preference.setFloat(SHARED_PREFERENCE_AMOUNT_WEEKLY, 0f)
            preference.setBoolean(SHARED_PREFERENCE_ALLOW_WEEK_RESET, false)
        } else if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY && !allowWeekReset) preference
                .setBoolean(SHARED_PREFERENCE_ALLOW_WEEK_RESET, true)

        //==========================================================================================
        // This checks id the months has changed.
        if (previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && extractMonthYear(currentDate)
                != extractMonthYear(previousDate)) {

            get<MWTDatabase>().let {
                if (monthAmount >= monthGoal) achievementLogic(it, currentDate, allowNotification, allowAchievementNotification,"Month")
                if (bmiRecordInterval == "month") bmiRecordLogic(it, currentDate, allowNotification, allowBMIRecordNotification)
            }
            preference.setFloat(SHARED_PREFERENCE_AMOUNT_MONTHLY, 0f)
        }
        //==========================================================================================

        preference.setString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, currentDate)
        return Result.success()
    }

    private fun extractMonthYear(date: String): String {
        val parseDate = date.split("-")
        return "${parseDate[0]}-${parseDate[1]}"
    }

    private fun sendOnNotificationIntake(allowNotification: Boolean,
                                         allowReminderNotification: Boolean) {
        if(allowNotification && allowReminderNotification){
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_INTAKE_WATER_ID)
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle("Track your water intake!")
                    .setContentText("Click here to open the  tracker.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build()
            notificationManager.notify(1, notification)
        }
    }

    private fun sendOnNotificationAchievement(allowNotification: Boolean,
                                              allowAchievementNotification: Boolean, type: String) {
        if (allowNotification && allowAchievementNotification) {
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ACHIEVEMENT_ID)
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle("You got a achievement!")
                    .setContentText("You got a new $type achievement.")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build()
            notificationManager.notify(2, notification)
        }
    }

    private fun sendOnNotificationRecordBMI(allowNotification: Boolean,
                                            allowBMIRecordNotification: Boolean) {
        if (allowNotification && allowBMIRecordNotification) {
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_RECORD_BMI_ID)
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle("Your BMI got recorded.")
                    .setContentText("Open the app to check your progress!")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build()
            notificationManager.notify(3, notification)
        }
    }

    private fun bmiRecordLogic(mwtDatabase: MWTDatabase, currentDate: String,
                               allowNotification: Boolean, allowBMIRecordNotification: Boolean) {
        mwtDatabase.bmiRecordDao().save(BMIRecordEntity(200f, currentDate))
        sendOnNotificationRecordBMI(allowNotification, allowBMIRecordNotification)
    }

    private fun achievementLogic(mwtDatabase: MWTDatabase, currentDate: String, allowNotification: Boolean,
                                 allowAchievementNotification: Boolean, type: String) {
        mwtDatabase.achievementsDao().save(AchievementsEntity("Reached $type goal",
                "$type: $currentDate", type))
        sendOnNotificationAchievement(allowNotification, allowAchievementNotification, type)
    }
}