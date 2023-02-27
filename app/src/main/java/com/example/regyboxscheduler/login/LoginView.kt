package com.example.regyboxscheduler.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.regyboxscheduler.ui.SchedulerTheme
import com.example.regyboxscheduler.ui.TopBar

@Composable
fun LoginView (
    onSignInRequest: (username: String, password: String) -> Unit,
    onInfoRequest: () -> Unit
) {
    val boxId = rememberSaveable { mutableStateOf("") }
    val currentBoxId = boxId.value

    val username = rememberSaveable { mutableStateOf("") }
    val currentUsername = username.value

    val password = rememberSaveable { mutableStateOf("") }
    val currentPassword = password.value

    SchedulerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onInfoRequested = { onInfoRequest() })
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Row {
                    Text(
                        text = "Regybox Scheduler",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Text(text = "Box Id:", modifier = Modifier.padding(8.dp))
                Row {
                    TextField(
                        value = currentBoxId,
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFFFFFFF),
                            textColor = Color(0xFF000000)
                        ),
                        onValueChange = { boxId.value = it }
                    )
                }
                Text(text = "Username:", modifier = Modifier.padding(8.dp))
                Row {
                    TextField(
                        value = currentUsername,
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFFFFFFF),
                            textColor = Color(0xFF000000)
                        ),
                        onValueChange = { username.value = it }
                    )
                }
                Text(text = "Password:", modifier = Modifier.padding(8.dp))
                Row {
                    TextField(
                        value = currentPassword,
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFFFFFFF),
                            textColor = Color(0xFF000000)
                        ),
                        onValueChange = { password.value = it },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
                Row {
                    Button(
                        onClick = { onSignInRequest(currentUsername, currentPassword) }
                    ) {
                        Text(text = "Login")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    LoginView(
        onSignInRequest = { _, _ -> },
        onInfoRequest = {}
    )
}