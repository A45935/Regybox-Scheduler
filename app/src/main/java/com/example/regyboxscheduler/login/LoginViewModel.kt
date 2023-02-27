package com.example.regyboxscheduler.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.regyboxscheduler.TAG
import com.example.regyboxscheduler.services.RegyboxServices
import com.example.regyboxscheduler.utils.SharedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val services: RegyboxServices
) : ViewModel() {

    fun login(boxId: String, username: String, password: String, callback: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cookie = services.login(boxId, username, password)
                if (cookie != null)
                    callback(cookie)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }
}
