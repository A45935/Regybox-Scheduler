package com.example.regyboxscheduler.boxSelector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.regyboxscheduler.DependenciesContainer
import com.example.regyboxscheduler.info.InfoActivity
import com.example.regyboxscheduler.login.LoginActivity
import com.example.regyboxscheduler.login.LoginViewModel
import com.example.regyboxscheduler.utils.viewModelInit
import kotlinx.coroutines.launch

class SelectorActivity : ComponentActivity()  {

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

        if (repo.sharedPrefs.selectedBox != null)
            LoginActivity.navigate(this)

        setContent {
            val boxes by viewModel.boxes.collectAsState()

            SelectorView(
                boxes.toList(),
                onInfoRequest = { InfoActivity.navigate(this) },
                onClassSelected = { selected ->
                    repo.sharedPrefs.selectedBox = selected
                    LoginActivity.navigate(this)
                }
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getBoxes()
            }
        }
    }
}