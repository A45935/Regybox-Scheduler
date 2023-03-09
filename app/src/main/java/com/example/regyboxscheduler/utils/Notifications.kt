package com.example.regyboxscheduler.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.regyboxscheduler.R

class Notifications (
    private val context: Context
) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun showNotification(id: Int, bigMessage: String, smallMessage: String) {
        val notification = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(bigMessage)
            .setContentText(smallMessage)
            .build()

        notificationManager.notify(id, notification)
    }

    fun removeNotification(id: Int) {
        notificationManager.cancel(id)
    }
}