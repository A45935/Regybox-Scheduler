package com.example.regyboxscheduler.scheduler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.regyboxscheduler.services.RegyboxServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*

private const val DAY = 86400

class ScheduleViewModel(
    private val services: RegyboxServices
) : ViewModel() {

    //current epoch time in seconds
    private val _timestamp = MutableStateFlow(System.currentTimeMillis()/1000)
    val timestamp = _timestamp.asStateFlow()

    private val _classes = MutableStateFlow<List<GymClass>>(emptyList())
    val classes = _classes.asStateFlow()

    fun getClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            //_classes.value =
                try {
                    services.getAulas(_timestamp.value.toString().padEnd(13, '0'))
                } catch (e: Exception) {
                    println(e.message)
                }
        }
    }

    fun scheduleClass(){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun cancelClass(){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun increaseDay(){
        _timestamp.value += DAY
        getClasses()
    }

    fun decreaseDay(){
        _timestamp.value -= DAY
        getClasses()
    }
}
