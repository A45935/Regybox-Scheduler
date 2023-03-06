package com.example.regyboxscheduler.scheduler

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.regyboxscheduler.ui.SchedulerTheme
import com.example.regyboxscheduler.ui.TopBar

@Composable
fun ScheduleView (
    onLogoutRequest: () -> Unit,
    onInfoRequest: () -> Unit,
    date: String,
    classes: List<GymClass>,
    nextDayClasses: () -> Unit,
    previousDayClasses: () -> Unit,
    scheduleClass: (GymClass) -> Unit,
    cancelClass: (GymClass) -> Unit
) {
    SchedulerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
                    onLogoutRequested = onLogoutRequest,
                    onInfoRequested = onInfoRequest
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
                Row {
                    Text(text = date, modifier = Modifier.padding(8.dp))
                }
                Row {
                    Button(onClick = previousDayClasses) { Text(text = "Previous day") }
                    Spacer(modifier = Modifier.width(32.dp))
                    Button(onClick = nextDayClasses) { Text(text = "Next day") }
                }
                if (classes.isEmpty()){
                    Text(text = "No classes to schedule", modifier = Modifier.padding(8.dp))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(classes.size) {
                            GymClassInfoView(
                                classInfo = classes[it],
                                onClassSelected = {
                                    if (!classes[it].scheduled)
                                        scheduleClass(classes[it])
                                    else
                                        cancelClass(classes[it])
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GymClassInfoView(
    classInfo: GymClass,
    onClassSelected: () -> Unit,
) {
    Card(
        backgroundColor = if (classInfo.scheduled) Color(0xFF009688) else Color(0xFFFFFFFF),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClassSelected() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = classInfo.nome,
                color = Color.Black,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = classInfo.hora,
                color = Color.Black,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SchedulerPreview() {
    ScheduleView (
        onLogoutRequest = {},
        onInfoRequest = {},
        date = "22/03/2023",
        classes = demoClasses,
        nextDayClasses = {},
        previousDayClasses = {},
        scheduleClass = {},
        cancelClass = {}
    )
}

private val demoClasses = buildList {
    repeat(10) {
        add(GymClass("$it", "Class $it", "$it PM", "", 123344, it%2 == 0))
    }
}

