package com.example.mwt

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mwt.util.UNIQUE_WORKER_NAME_TRACKER
import java.util.concurrent.TimeUnit

class MWTApplication() : Application() {

    override fun onCreate() {
        super.onCreate()
        enqueueWork()
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
}
