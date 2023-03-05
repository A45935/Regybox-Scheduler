package com.example.regyboxscheduler.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class SchedulerBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            intent?.let {
                val timestamp = it.getStringExtra("TIMESTAMP")
                val classId = it.getStringExtra("ID")

                val scheduleWorkRequest = OneTimeWorkRequestBuilder<SchedulerWorker>()
                    .setInputData(workDataOf("TIMESTAMP" to timestamp, "ID" to classId))
                    .build()

                WorkManager.getInstance(context.applicationContext).enqueue(scheduleWorkRequest)
            }
        }
    }
}