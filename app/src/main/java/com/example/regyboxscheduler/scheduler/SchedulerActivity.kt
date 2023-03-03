package com.example.regyboxscheduler.scheduler

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
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
import com.example.regyboxscheduler.utils.AlarmScheduler
import com.example.regyboxscheduler.utils.SchedulerBroadcastReceiver
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

    private fun scheduleClass(selectedClass: GymClass) {
        alarmScheduler.schedule(selectedClass)

        repo.sharedPrefs.prefs
            .edit()
            .putInt(selectedClass.classId, selectedClass.hashCode())
            .apply()

        viewModel.getClasses()
    }

    private fun cancelSchedule(selectedClass: GymClass) {
        alarmScheduler.cancel(selectedClass)

        repo.sharedPrefs.prefs
            .edit()
            .remove(selectedClass.classId)
            .apply()

        viewModel.getClasses()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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