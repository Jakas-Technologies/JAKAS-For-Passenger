package com.miftah.jakasforpassenger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.miftah.jakasforpassenger.core.provider.LocationClient
import com.miftah.jakasforpassenger.core.workers.FindRouteWorker
import com.miftah.jakasforpassenger.core.workers.FindUserWorker
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.miftah.jakasforpassenger.utils.Constants.NOTIFICATION_CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: AppWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

}

class AppWorkerFactory @Inject constructor(private val locationClient: LocationClient) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return when (workerClassName) {
            FindRouteWorker::class.java.name -> FindRouteWorker(
                context = appContext,
                workerParams = workerParameters
            )

            FindUserWorker::class.java.name -> FindUserWorker(
                locationClient = locationClient,
                context = appContext,
                workerParams = workerParameters
            )

            else -> throw Throwable("Unknown Worker class: $workerClassName" +
                    "${FindRouteWorker::class.simpleName}")
        }
    }

}