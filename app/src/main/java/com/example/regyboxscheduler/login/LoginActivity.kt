package com.example.regyboxscheduler.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.regyboxscheduler.DependenciesContainer
import com.example.regyboxscheduler.info.InfoActivity
import com.example.regyboxscheduler.scheduler.SchedulerActivity
import com.example.regyboxscheduler.utils.Cookies
import com.example.regyboxscheduler.utils.viewModelInit

class LoginActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer)
    }

    private val viewModel by viewModels<LoginViewModel> {
        viewModelInit {
            LoginViewModel(repo.regyboxServices)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (repo.sharedPrefs.cookies != null)
            SchedulerActivity.navigate(this)

        setContent {
            LoginView(
                onLoginRequest = { boxId, username, password ->
                    viewModel.login(boxId, username, password) { cookie ->
                        if(cookie != null) {
                            repo.sharedPrefs.cookies = Cookies("%2A$cookie", cookie)
                            SchedulerActivity.navigate(this)
                        } else {
                            runOnUiThread {
                                Toast.makeText(this, "Invalid parameters", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                onInfoRequest = { InfoActivity.navigate(this) }
            )
        }
    }
}