package com.example.mwt

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
        val timeInterval = preference.getInt(SHARED_PREFERENCE_TIME_INTERVAL, DEFAULT_TIME_INTERVAL)
        val timeIntervalTracker = preference.getInt(SHARED_PREFERENCE_TIME_INTERVAL_TRACKER, DEFAULT_TIME_INTERVAL)

        if (timeInterval == timeIntervalTracker){
            sendOnNotificationIntake(allowReminderNotification)
            preference.setInt(SHARED_PREFERENCE_TIME_INTERVAL_TRACKER, 15)
        }else{
            preference.setInt(SHARED_PREFERENCE_TIME_INTERVAL_TRACKER, timeIntervalTracker + 15)
        }

        //==========================================================================================
        // This checks if the day has changed.
        if (previousDate != DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE && previousDate != currentDate) {
            get<MWTDatabase>().let {
                if (dayAmount >= dayGoal) achievementLogic(it, currentDate, allowAchievementNotification, "Day")
                it.dateProgressDao().save(DateProgressEntity(previousDate, dayAmount))
                it.dailyLogDao().dropTable()
                if (bmiRecordInterval == "day") bmiRecordLogic(it, currentDate, allowBMIRecordNotification)
            }
            preference.setFloat(SHARED_PREFERENCE_AMOUNT_DAILY, 0f)
        }
        //==========================================================================================
        // This checks if the week has changed.
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && allowWeekReset) {
            get<MWTDatabase>().let {
                if (weekAmount >= weekGoal) achievementLogic(it, currentDate, allowAchievementNotification, "Week")
                if (bmiRecordInterval == "week") bmiRecordLogic(it, currentDate, allowBMIRecordNotification)
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
                if (monthAmount >= monthGoal) achievementLogic(it, currentDate, allowAchievementNotification, "Month")
                if (bmiRecordInterval == "month") bmiRecordLogic(it, currentDate, allowBMIRecordNotification)
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

    private fun sendOnNotificationIntake(allowReminderNotification: Boolean) {
        if (allowReminderNotification) {

            val activityIntent = Intent(applicationContext, MainActivity::class.java)
            activityIntent.putExtra(ACTIVITY_SELECTION_NOTIFICATION, ACTIVITY_SELECTION_NOTIFICATION_INTAKE)
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0,
                    activityIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_INTAKE_WATER_ID)
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle("Track your water intake!")
                    .setContentText("Click here to open the tracker.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            notificationManager.notify(NOTIFICATION_ID_INTAKE, notification)
        }
    }

    private fun sendOnNotificationAchievement(allowAchievementNotification: Boolean) {
        if (allowAchievementNotification) {

            val activityIntent = Intent(applicationContext, MainActivity::class.java)
            activityIntent.putExtra(ACTIVITY_SELECTION_NOTIFICATION, ACTIVITY_SELECTION_NOTIFICATION_ACHIEVEMENT)
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0,
                    activityIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ACHIEVEMENT_ID)
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle("You got a achievement!")
                    .setContentText("Click here to see your new achievements.")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            notificationManager.notify(NOTIFICATION_ID_ACHIEVEMENT, notification)
        }
    }

    private fun sendOnNotificationRecordBMI(allowBMIRecordNotification: Boolean) {
        if (allowBMIRecordNotification) {

            val activityIntent = Intent(applicationContext, MainActivity::class.java)
            activityIntent.putExtra(ACTIVITY_SELECTION_NOTIFICATION, ACTIVITY_SELECTION_NOTIFICATION_BMI)
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0,
                    activityIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_RECORD_BMI_ID)
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle("Your BMI got recorded.")
                    .setContentText("Click here to check your BMI records.")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            notificationManager.notify(NOTIFICATION_ID_BMI_RECORD, notification)
        }
    }

    private fun bmiRecordLogic(mwtDatabase: MWTDatabase, currentDate: String,
                               allowBMIRecordNotification: Boolean) {
        mwtDatabase.bmiRecordDao().save(BMIRecordEntity(200f, currentDate))
        sendOnNotificationRecordBMI(allowBMIRecordNotification)
    }

    private fun achievementLogic(mwtDatabase: MWTDatabase, currentDate: String,
                                 allowAchievementNotification: Boolean, type: String) {
        mwtDatabase.achievementsDao().save(AchievementsEntity("Reached $type goal",
                "$type: $currentDate", type))
        sendOnNotificationAchievement(allowAchievementNotification)
    }
}