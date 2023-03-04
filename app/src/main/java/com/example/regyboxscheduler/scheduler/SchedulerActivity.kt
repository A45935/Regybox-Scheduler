package com.example.regyboxscheduler.scheduler

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.regyboxscheduler.DependenciesContainer
import com.example.regyboxscheduler.R
import com.example.regyboxscheduler.alarms.AlarmScheduler
import com.example.regyboxscheduler.utils.viewModelInit
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SchedulerActivity: ComponentActivity() {
    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, SchedulerActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val repo by lazy {
        (application as DependenciesContainer)
    }

    private val viewModel by viewModels<ScheduleViewModel> {
        viewModelInit {
            ScheduleViewModel(repo.regyboxServices, repo.sharedPrefs)
        }
    }

    private val alarmScheduler by lazy {
        AlarmScheduler(this)
    }

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun scheduleClass(selectedClass: GymClass) {
        val uuid = alarmScheduler.schedule(selectedClass)

        repo.sharedPrefs.prefs
            .edit()
            .putInt(selectedClass.classId, uuid)
            .apply()

        viewModel.getClasses()

        showNotification("Class Scheduled", "Auto Schedule for ${selectedClass.nome} at ${selectedClass.hora}")
    }

    private fun cancelSchedule(selectedClass: GymClass) {
        val uuid = repo.sharedPrefs.prefs.getInt(selectedClass.classId, 0)

        alarmScheduler.cancel(uuid)

        repo.sharedPrefs.prefs
            .edit()
            .remove(selectedClass.classId)
            .apply()

        viewModel.getClasses()

        removeNotification()
    }

    private fun showNotification(bigMessage: String, smallMessage: String) {
        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(bigMessage)
            .setContentText(smallMessage)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun removeNotification() {
        notificationManager.cancel(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel name",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        setContent {
            val timestamp by viewModel.timestamp.collectAsState()
            val classes by viewModel.classes.collectAsState()

            ScheduleView(
                onLogoutRequest = {
                    repo.sharedPrefs.cookies = null
                    finish() 
                },
                date = SimpleDateFormat("E dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp * 1000)) ,
                classes = classes,
                nextDayClasses = { viewModel.increaseDay() },
                previousDayClasses = { viewModel.decreaseDay() },
                scheduleClass = { selected -> scheduleClass(selected) },
                cancelClass = { selected -> cancelSchedule(selected) }
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getClasses()
            }
        }
    }
}

data class GymClass(val classId: String, val nome: String, val hora: String, val scheduleTime: Long, var scheduled: Boolean = false)