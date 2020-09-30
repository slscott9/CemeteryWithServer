package com.example.cemeterywithserver

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.cemeterywithserver.worker.CemeteryRefreshWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CemeteryApplication : Application(), Configuration.Provider{

    val applicationScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupRecurringWork()
    }


    //Adds constraints for when work happens, sets up when work executes, and enqueues work if it has not finished
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<CemeteryRefreshWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            CemeteryRefreshWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE, //keep means if we have created this work before just keep it, do not create new work again
            repeatingRequest
        )
    }

    override fun getWorkManagerConfiguration(): Configuration  =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()

}