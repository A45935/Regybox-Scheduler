package com.example.regyboxscheduler

import android.app.Application
import android.util.Log
import com.example.regyboxscheduler.services.RegyboxServices
import com.example.regyboxscheduler.utils.SharedPrefs
import okhttp3.OkHttpClient
import org.jsoup.Jsoup

const val TAG = "RegyboxScheduler"
const val APP_VERSION = "1.0.0"

interface DependenciesContainer {
    val regyboxServices: RegyboxServices
    val sharedPrefs: SharedPrefs
}

class SchedulerApplication : DependenciesContainer, Application() {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    override val regyboxServices: RegyboxServices
        get() = RegyboxServices(httpClient, sharedPrefs)
    override val sharedPrefs: SharedPrefs
        get() = SharedPrefs(this)
}