package com.example.regyboxscheduler.info

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.regyboxscheduler.APP_VERSION
import com.example.regyboxscheduler.ui.SchedulerTheme
import com.example.regyboxscheduler.ui.TopBar
import com.example.regyboxscheduler.R

@Composable
fun InfoView (
    onBackRequest: () -> Unit,
    onSendEmailRequested: (String) -> Unit = { },
    onUrlRequested: (Uri) -> Unit = { }
) {
    SchedulerTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("AboutView"),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar( onBackRequested = onBackRequest ) }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Version $APP_VERSION",
                        style = MaterialTheme.typography.h6
                    )
                }
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "made by David Costa",
                        style = MaterialTheme.typography.h6
                    )
                }
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(255,255,255)),
                        onClick = { onSendEmailRequested("a45935@alunos.isel.pt") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.email_logo),
                            contentDescription = null,
                            modifier = Modifier.sizeIn(25.dp, 25.dp, 50.dp, 50.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(255,255,255)),
                        onClick = { onUrlRequested(Uri.parse("https://github.com/A45935")) }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.github_logo),
                            contentDescription = null,
                            modifier = Modifier.sizeIn(25.dp, 25.dp, 50.dp, 50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutPreview() {
    InfoView(
        onBackRequest = {},
        onSendEmailRequested = {},
        onUrlRequested = {}
    )
}