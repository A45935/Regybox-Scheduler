package com.example.regyboxscheduler.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.regyboxscheduler.DependenciesContainer
import com.example.regyboxscheduler.info.InfoActivity
import com.example.regyboxscheduler.services.RegyboxServices
import com.example.regyboxscheduler.utils.viewModelInit

class LoginActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LoginViewModel(app.regyboxServices)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginView(
                onLoginRequest = { boxId, username, password ->
                    viewModel.login(boxId, username, password)
                },
                onInfoRequest = { InfoActivity.navigate(this) }
            )
        }
    }
}