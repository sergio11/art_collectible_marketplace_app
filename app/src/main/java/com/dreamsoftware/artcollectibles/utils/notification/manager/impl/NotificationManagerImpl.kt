package com.dreamsoftware.artcollectibles.utils.notification.manager.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.dreamsoftware.artcollectibles.services.INotificationService
import com.dreamsoftware.artcollectibles.services.core.NotificationBackgroundServiceBinder
import com.dreamsoftware.artcollectibles.utils.notification.manager.INotificationManager

@Suppress("UNCHECKED_CAST")
class NotificationManagerImpl(
    private val notificationBackgroundServiceIntent: Intent
): INotificationManager {

    private companion object {
        const val TAG = "NotificationManager"
    }

    private var notificationService: INotificationService? = null
    // Tracks the bound state of the service.
    private var mBound = false

    // Monitors the state of the connection to the service.
    private val mServiceConnection = object : ServiceConnection {

        /**
         * On Service Connected
         */
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as? NotificationBackgroundServiceBinder
            binder?.provideService()?.let {
                notificationService = it
                mBound = true
            }
        }

        /**
         * On Service Disconnected
         */
        override fun onServiceDisconnected(name: ComponentName) {
            notificationService = null
            mBound = false
        }
    }

    /**
     *  Bind to the service. If the service is in foreground mode, this signals to the service
     * that since this activity is in the foreground, the service can exit foreground mode.
     */
    override fun bind(context: Context) {
        Log.d(TAG, "NotificationManagerImpl - bind")
        context.apply {
            startService(notificationBackgroundServiceIntent)
            bindService(notificationBackgroundServiceIntent,
                mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Unbind from the service. This signals to the service that this activity is no longer
     * in the foreground, and the service can respond by promoting itself to a foreground
     * service.
     */
    override fun unBind(context: Context) {
        Log.d(TAG, "NotificationManagerImpl - unBind - mBound ($mBound)")
        if (mBound) {
            context.unbindService(mServiceConnection)
            mBound = false
        }
    }
}