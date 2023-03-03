package com.example.regyboxscheduler.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.*

class SchedulerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            intent?.let {
                val timestamp = it.getStringExtra("TIMESTAMP")
                val classId = it.getStringExtra("ID")

                val scheduleWorkRequest = OneTimeWorkRequestBuilder<SchedulerWorker>()
                    .setInputData(workDataOf("TIMESTAMP" to timestamp))
                    .setInputData(workDataOf("ID" to classId))
                    .build()

                WorkManager.getInstance(context.applicationContext).enqueue(scheduleWorkRequest)
            }
        }
    }
}