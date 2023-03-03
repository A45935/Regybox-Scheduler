package com.example.regyboxscheduler

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.regyboxscheduler.services.RegyboxServices
import com.example.regyboxscheduler.repository.SchedulerDatabase
import com.example.regyboxscheduler.repository.SchedulerRepository
import com.example.regyboxscheduler.utils.SharedPrefs
import okhttp3.OkHttpClient

const val TAG = "RegyboxScheduler"
const val APP_VERSION = "1.0.0"

interface DependenciesContainer {
    val regyboxServices: RegyboxServices
    val sharedPrefs: SharedPrefs
    val database: SchedulerRepository
    val alarmManager: AlarmManager
}

class SchedulerApplication : DependenciesContainer, Application() {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    override val database: SchedulerRepository
        get() = SchedulerRepository(this)

    override val alarmManager: AlarmManager
        get() = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override val regyboxServices: RegyboxServices
        get() = RegyboxServices(httpClient, sharedPrefs)

    override val sharedPrefs: SharedPrefs
        get() = SharedPrefs(this)
}