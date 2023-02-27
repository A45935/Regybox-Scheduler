package com.example.regyboxscheduler.scheduler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScheduleView(
                onLogoutRequest = {
                    //TODO del cookies
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