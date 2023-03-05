package com.example.regyboxscheduler.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.regyboxscheduler.ui.SchedulerTheme
import com.example.regyboxscheduler.ui.TopBar

@Composable
fun LoginView (
    boxes: HashMap<String, String>,
    onLoginRequest: (boxId: String, username: String, password: String) -> Unit,
    onInfoRequest: () -> Unit
) {
    val boxId = rememberSaveable { mutableStateOf("") }
    val currentBoxId = boxId.value

    val username = rememberSaveable { mutableStateOf("") }
    val currentUsername = username.value

    val password = rememberSaveable { mutableStateOf("") }
    val currentPassword = password.value

    var expanded by remember { mutableStateOf(false) }

    // store the selected box
    var selectedText by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

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
                Text(text = "Select your box", modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = selectedText,
                    onValueChange = { selectedText = it },
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFFFFFFF),
                        textColor = Color(0xFF000000)
                    ),
                    singleLine = true
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    boxes.forEach { box ->
                        DropdownMenuItem(onClick = {
                            selectedText = box.value
                            expanded = false
                        }) {
                            Text(text = box.key)
                        }
                    }
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
                        onClick = { onLoginRequest(currentBoxId, currentUsername, currentPassword) }
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
        boxes = hashMapOf(),
        onLoginRequest = { _, _, _ -> },
        onInfoRequest = {}
    )
}