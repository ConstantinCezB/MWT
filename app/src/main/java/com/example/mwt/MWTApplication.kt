package com.example.mwt

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mwt.util.MWTModule
import com.example.mwt.util.UNIQUE_WORKER_NAME_TRACKER
import org.koin.android.ext.android.startKoin
import java.util.concurrent.TimeUnit

class MWTApplication() : Application() {
    companion object {
        const val CHANNEL_INTAKE_WATER_ID = "channelIntake"
        const val CHANNEL_ACHIEVEMENT_ID = "channelAchievement"
        const val CHANNEL_RECORD_BMI_ID = "channelRecordBmi"
    }


    override fun onCreate() {
        super.onCreate()
        enqueueWork()
        createNotificationChannels()
        startKoin(this, listOf(MWTModule))
    }

    private fun enqueueWork() {
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                        UNIQUE_WORKER_NAME_TRACKER,
                        ExistingPeriodicWorkPolicy.KEEP,
                        PeriodicWorkRequest.Builder(
                                TrackerWorkerManager::class.java,
                                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                                TimeUnit.MILLISECONDS
                        ).build()
                )
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannels = mutableListOf<NotificationChannel>()

            val intake = NotificationChannel(
                    CHANNEL_INTAKE_WATER_ID,
                    "Intake notifications",
                    NotificationManager.IMPORTANCE_HIGH
            )
            intake.description = "This allows water drinking reminders to be passed to the user."
            notificationChannels += intake

            val achievement = NotificationChannel(
                    CHANNEL_ACHIEVEMENT_ID,
                    "Achievement notifications",
                    NotificationManager.IMPORTANCE_LOW
            )
            achievement.description = "This allows the user to know when he gets achievements."
            notificationChannels += achievement

            val recordBMI = NotificationChannel(
                    CHANNEL_RECORD_BMI_ID,
                    "BMI record notifications",
                    NotificationManager.IMPORTANCE_LOW
            )
            recordBMI.description = "This allows the user to know when BMI is recorded."
            notificationChannels += recordBMI

            getSystemService(NotificationManager::class.java)
                    .createNotificationChannels(notificationChannels)

        }
    }
}
