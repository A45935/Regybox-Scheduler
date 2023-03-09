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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.regyboxscheduler.DependenciesContainer
import com.example.regyboxscheduler.alarms.AlarmScheduler
import com.example.regyboxscheduler.info.InfoActivity
import com.example.regyboxscheduler.utils.Notifications
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

    private val repo by lazy { (application as DependenciesContainer) }

    private val viewModel by viewModels<ScheduleViewModel> {
        viewModelInit {
            ScheduleViewModel(repo.regyboxServices, repo.sharedPrefs)
        }
    }

    private val alarmScheduler by lazy { AlarmScheduler(this) }

    private val notificationManager by lazy { Notifications(this) }

    private fun scheduleClass(selectedClass: GymClass) {
        val uuid = alarmScheduler.schedule(selectedClass)

        repo.sharedPrefs.prefs
            .edit()
            .putInt(repo.sharedPrefs.cookies!!.user + selectedClass.classId, uuid)
            .apply()

        viewModel.getClasses()

        notificationManager.showNotification(selectedClass.classId.toInt(),"Class Scheduled", "Auto Schedule for ${selectedClass.nome} at ${selectedClass.hora}")
    }

    private fun cancelSchedule(selectedClass: GymClass) {
        val uuid = repo.sharedPrefs.prefs.getInt(repo.sharedPrefs.cookies!!.user + selectedClass.classId, 0)

        alarmScheduler.cancel(uuid)

        repo.sharedPrefs.prefs
            .edit()
            .remove(repo.sharedPrefs.cookies!!.user + selectedClass.classId)
            .apply()

        viewModel.getClasses()

        notificationManager.removeNotification(selectedClass.classId.toInt())
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
                onInfoRequest = { InfoActivity.navigate(this) },
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

data class GymClass(val classId: String, val nome: String, val hora: String, val date: String, val scheduleTime: Long, var scheduled: Boolean = false)