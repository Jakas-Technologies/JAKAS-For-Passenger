package com.miftah.jakasforpassenger.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.miftah.jakasforpassenger.core.provider.LocationClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FindUserWorker @AssistedInject constructor(
    private val locationClient: LocationClient,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return Result.success()

    }


}