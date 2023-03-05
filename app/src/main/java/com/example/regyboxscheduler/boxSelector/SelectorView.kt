package com.example.regyboxscheduler.boxSelector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.regyboxscheduler.ui.SchedulerTheme
import com.example.regyboxscheduler.ui.TopBar
import com.example.regyboxscheduler.utils.Box

@Composable
fun SelectorView(
    boxes: List<Pair<String, String>>,
    onInfoRequest: () -> Unit,
    onClassSelected: (Box) -> Unit
) {
    val search = rememberSaveable { mutableStateOf("") }
    val currentSearch = search.value

    SchedulerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar( onInfoRequested = onInfoRequest )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Row {
                    TextField(
                        value = currentSearch,
                        singleLine = true,
                        onValueChange = {
                            search.value = it
                        }
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    val filteredBoxes = if(currentSearch == "") boxes else boxes.filter { it.first.startsWith(currentSearch, true) }
                    items(filteredBoxes.size) {
                        BoxInfoView(
                            boxInfo = filteredBoxes[it],
                            onClassSelected = { selected -> onClassSelected(selected) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoxInfoView(
    boxInfo: Pair<String, String>,
    onClassSelected: (Box) -> Unit
) {
    Card(
        backgroundColor = Color(0xFFFFFFFF),
        shape = MaterialTheme.shapes.small,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClassSelected(Box(boxInfo.first, boxInfo.second)) }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = boxInfo.first,
                color = Color.Black,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}