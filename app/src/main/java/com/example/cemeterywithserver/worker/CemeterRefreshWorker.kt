package com.example.cemeterywithserver.worker

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cemeterywithserver.repositories.CemeteryRepository
import timber.log.Timber
import java.lang.Exception

class CemeteryRefreshWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: CemeteryRepository
) : CoroutineWorker(context, workerParameters) {

//Coroutine worker by default runs on Dispatchers.default

    companion object {
        const val WORK_NAME = "cemetery_refresh_worker"
    }
    /*

        get all cemeteries that are not synched, send them to the server.
        if success set all cemeteries to synched and re insert them into database
 */
    override suspend fun doWork(): Result {
        Timber.i("Starting doWork")
        try {

            val unSynchedCems = repository.getunSynchedCemeteries()
            Timber.i(unSynchedCems.toString())
            val response = repository.sendNewCemsToNetwork(unSynchedCems)
            if(response.isSuccessful){

                val synchedCemeteries = unSynchedCems.map {
                    it.apply { isSynced = true }
                }
                Timber.i(synchedCemeteries.toString())

                repository.insertCemeteryList(synchedCemeteries)
            }

        } catch (e: Exception) {
            Timber.i( e.message.toString())

            return Result.retry()
        }
        return Result.success()
    }

}