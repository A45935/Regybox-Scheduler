package com.example.regyboxscheduler.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.regyboxscheduler.TAG
import com.example.regyboxscheduler.services.RegyboxServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val services: RegyboxServices
) : ViewModel() {

    fun login(boxId: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                services.login(boxId, username, password)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }
}
