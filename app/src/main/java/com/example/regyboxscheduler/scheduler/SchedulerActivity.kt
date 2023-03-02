package com.example.regyboxscheduler.scheduler

import android.app.Activity
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
import androidx.work.*
import com.example.regyboxscheduler.DependenciesContainer
import com.example.regyboxscheduler.utils.SchedulerWorker
import com.example.regyboxscheduler.utils.viewModelInit
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
            ScheduleViewModel(repo.regyboxServices)
        }
    }

    private fun scheduleClass(selectedClass: GymClass, timestamp: Long) {
        val scheduleWorkRequest = OneTimeWorkRequestBuilder<SchedulerWorker>()
            .setInputData(workDataOf("TIMESTAMP" to timestamp))
            .setInputData(workDataOf("ID" to selectedClass.classId))
            .setInitialDelay(selectedClass.scheduleTime, TimeUnit.SECONDS)
            .build()

        val workerId = scheduleWorkRequest.id
        //TODO add to database mapped to classId

        WorkManager.getInstance(this).enqueue(scheduleWorkRequest)
    }

    private fun cancelSchedule(selectedClass: GymClass) {
        //selectedClass.scheduled = !selectedClass.scheduled
        val workerId = UUID.randomUUID() //TODO get workerId from database
        WorkManager.getInstance(this).cancelWorkById(workerId)
        //TODO remove entry from database
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
                scheduleClass = { selected -> scheduleClass(selected, timestamp) },
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