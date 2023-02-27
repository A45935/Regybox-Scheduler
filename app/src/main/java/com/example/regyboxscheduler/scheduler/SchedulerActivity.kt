package com.example.regyboxscheduler.scheduler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.regyboxscheduler.DependenciesContainer
import com.example.regyboxscheduler.login.LoginViewModel
import com.example.regyboxscheduler.utils.viewModelInit
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

    private val viewModel by viewModels<LoginViewModel> {
        viewModelInit {
            ScheduleViewModel(repo.regyboxServices)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScheduleView(
                onLogoutRequest = {
                    repo.sharedPrefs.cookies = null
                    finish() 
                },
                date = Date(),
                classes = emptyList(),
                nextDayClasses = {},
                previousDayClasses = {},
                scheduleClass = {},
                cancelClass = {}
            )
        }
    }
}

data class GymClass (val classId: String, val nome: String, val hora: String)