package com.example.regyboxscheduler.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.regyboxscheduler.scheduler.GymClass

class AlarmScheduler (
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(selectedClass: GymClass) {
        // Intent to start the Broadcast Receiver
        val broadcastIntent =
            Intent(context, SchedulerBroadcastReceiver::class.java).apply {
                putExtra("TIMESTAMP", selectedClass.scheduleTime.toString())
                putExtra("ID", selectedClass.classId)
            }

        // The Pending Intent to pass in AlarmManager
        val pIntent = PendingIntent.getBroadcast(
            context,
            selectedClass.hashCode(),
            broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedClass.scheduleTime * 1000, pIntent)
    }

    fun cancel(selectedClass: GymClass) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                selectedClass.hashCode(),
                Intent(context, SchedulerBroadcastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}