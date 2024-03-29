package com.example.regyboxscheduler.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.regyboxscheduler.scheduler.GymClass
import java.util.UUID

class AlarmScheduler (
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(selectedClass: GymClass): Int {
        val uuid = UUID.randomUUID().hashCode()

        // Intent to start the Broadcast Receiver
        val broadcastIntent =
            Intent(context, SchedulerBroadcastReceiver::class.java)
                .putExtra("DATE", selectedClass.date)
                .putExtra("ID", selectedClass.classId)

        // The Pending Intent to pass in AlarmManager
        val pIntent = PendingIntent.getBroadcast(
            context,
            uuid,
            broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedClass.scheduleTime * 1000, pIntent)
        return uuid
    }

    fun cancel(uuid: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                uuid,
                Intent(context, SchedulerBroadcastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}