package com.dreamsoftware.artcollectibles.services

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMintedEvent
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetEventsUseCase
import com.dreamsoftware.artcollectibles.services.core.SupportServiceBinder
import com.dreamsoftware.artcollectibles.utils.AppEventBus
import com.dreamsoftware.artcollectibles.utils.notification.ui.IUINotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBackgroundService : LifecycleService(), INotificationService {

    @Inject
    lateinit var notificationHelper: IUINotificationHelper

    @Inject
    lateinit var getEventsUseCase: GetEventsUseCase

    @Inject
    lateinit var appEventBus: AppEventBus

    private var isEventsSubscriptionActive = false
    private lateinit var eventsCollectorJob: Job
    private lateinit var appsEventsCollectorJob: Job


    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var mChangingConfiguration = false
    private lateinit var mServiceHandler: Handler

    /**
     * Local Binder
     */
    private val localBinder = LocalBinder()

    /**
     * On Configuration Changed
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(SERVICE_TAG, "NotificationBackgroundService - onCreate")
        // Create Service Handler
        mServiceHandler = createServiceHandler()
        subscribeToAppEvents()
    }

    /**
     * On Start Command
     */

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(SERVICE_TAG, "NotificationBackgroundService - onStartCommand")
        intent?.let {
            val startedFromNotification = it.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false)
            if (startedFromNotification) {
                stopSelf()
            }
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY
    }

    /**
     * On Bind
     */
    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.d(SERVICE_TAG, "NotificationBackgroundService - onBind")
        stopForegroundService()
        mChangingConfiguration = false
        return localBinder
    }

    /**
     * On Rebind
     */
    override fun onRebind(intent: Intent) {
        Log.d(SERVICE_TAG, "NotificationBackgroundService - onRebind")
        stopForegroundService()
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    /**
     * On Unbind
     * Last client unbound from service therefore starting service
     * again as foreground
     */
    override fun onUnbind(intent: Intent): Boolean {
        Log.d(SERVICE_TAG, "NotificationBackgroundService - onUnbind")
        if (!mChangingConfiguration && isEventsSubscriptionActive) {
            val notification = getNotification()
            Log.d(SERVICE_TAG, "NotificationBackgroundService - startForeground")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(FOREGROUND_NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_DATA_SYNC)
            } else {
                startForeground(FOREGROUND_NOTIFICATION_ID, notification)
            }
        }
        return true
    }

    /**
     * On Destroy
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(SERVICE_TAG, "NotificationBackgroundService - onDestroy")
        mServiceHandler.removeCallbacksAndMessages(null)
        if(isEventsSubscriptionActive) {
            unSubscribeFromEvents()
        }
    }

    /**
     * Create Service Handler
     */
    private fun createServiceHandler(): Handler =
        Handler(HandlerThread(SERVICE_TAG).also { it.start() }.looper)

    private fun getNotification(): Notification =
        notificationHelper.createImportantNotification(
            smallIcon = R.mipmap.ic_launcher,
            title = "Notification Background Service",
            body = "Notification Background Service work in background",
            intent = getServiceIntent().apply {
                // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
                putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)
            }
        )

    private fun getServiceIntent(): Intent = getServiceIntent(this)

    private fun stopForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
    }

    private fun subscribeToAppEvents() {
        appsEventsCollectorJob = lifecycleScope.launch {
            appEventBus.events.collectLatest {
                Log.d(SERVICE_TAG, "appEventBus.events $it CALLED!")
                if(it == AppEventBus.AppEvent.SIGN_IN) {
                    subscribeToEvents()
                } else if(it == AppEventBus.AppEvent.SIGN_OUT) {
                    unSubscribeFromEvents()
                }
            }
        }
    }

    private fun subscribeToEvents() {
        lifecycleScope.launch {
            getEventsUseCase.launch()
        }
        eventsCollectorJob = lifecycleScope.launch {
            getEventsUseCase.resultFlow.flowOn(Dispatchers.IO).collectLatest {
                Log.d(SERVICE_TAG, "resultFlow.collectLatest CALLED!")
                if(it is ArtCollectibleMintedEvent) {
                    Log.d(SERVICE_TAG, "event -> ArtCollectibleMintedEvent CALLED!")
                }
            }
        }
        isEventsSubscriptionActive = true
    }

    private fun unSubscribeFromEvents() {
        isEventsSubscriptionActive = false
        eventsCollectorJob.cancel()
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : SupportServiceBinder<INotificationService>() {
        override fun provideService(): INotificationService =
            this@NotificationBackgroundService
    }

    companion object {
        private const val SERVICE_TAG = "NotificationBackgroundService"
        private const val FOREGROUND_NOTIFICATION_ID = 1234567

        /**
         * Extra Started From Notification
         */
        private const val EXTRA_STARTED_FROM_NOTIFICATION = "STARTED_FROM_NOTIFICATION"

        @JvmStatic
        fun getServiceIntent(context: Context) =
            Intent(context, NotificationBackgroundService::class.java)
    }
}