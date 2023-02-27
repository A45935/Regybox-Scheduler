package com.example.regyboxscheduler.scheduler

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.regyboxscheduler.ui.SchedulerTheme
import com.example.regyboxscheduler.ui.TopBar
import java.util.Date

@Composable
fun ScheduleView (
    onLogoutRequest: () -> Unit,
    date: Date,
    classes: List<GymClass>,
    nextDayClasses: () -> Unit,
    previousDayClasses: () -> Unit,
    scheduleClass: (String) -> Unit,
    cancelClass: (String) -> Unit
) {
    SchedulerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar( onLogoutRequested = onLogoutRequest )
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
                    Text(text = date.toString(), modifier = Modifier.padding(8.dp))
                    Button(onClick = previousDayClasses) { Text(text = "Previous day") }
                    Button(onClick = nextDayClasses) { Text(text = "Next day") }
                }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(classes.size) {
                        GymClassInfoView(
                            classInfo = classes[it],
                            onClassSelected = { scheduleClass(classes[it].classId) }
                        )
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
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = classInfo.hora,
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