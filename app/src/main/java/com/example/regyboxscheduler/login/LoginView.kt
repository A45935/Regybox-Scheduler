package com.example.regyboxscheduler.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.regyboxscheduler.ui.SchedulerTheme
import com.example.regyboxscheduler.ui.TopBar

@Composable
fun LoginView (
    boxName: String,
    onLoginRequest: (username: String, password: String) -> Unit,
    onInfoRequest: () -> Unit,
    onBackRequest: () -> Unit
) {

    val username = rememberSaveable { mutableStateOf("") }
    val currentUsername = username.value

    val password = rememberSaveable { mutableStateOf("") }
    val currentPassword = password.value

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    SchedulerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
                    onInfoRequested = { onInfoRequest() },
                    onBackRequested = { onBackRequest() }
                )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(text = boxName, modifier = Modifier.padding(8.dp))

                Text(text = "Username:", modifier = Modifier.padding(8.dp))

                Row {
                    TextField(
                        value = currentUsername,
                        singleLine = true,
                        onValueChange = { username.value = it }
                    )
                }

                Text(text = "Password:", modifier = Modifier.padding(8.dp))

                Row {
                    TextField(
                        value = currentPassword,
                        singleLine = true,
                        onValueChange = { password.value = it },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                            val description = if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description)
                            }
                        }
                    )
                }

                Row {
                    Button(
                        onClick = { onLoginRequest(currentUsername, currentPassword) }
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
        boxName = "Hero Box",
        onLoginRequest = { _, _ -> },
        onInfoRequest = {},
        onBackRequest = {}
    )
}