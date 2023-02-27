package com.example.regyboxscheduler.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

@Composable
fun TopBar(
    onBackRequested: (() -> Unit)? = null,
    onInfoRequested: (() -> Unit)? = null,
    onLogoutRequested: (() -> Unit)? = null,
    onRefreshRequested: (() -> Unit)? = null
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            if (onBackRequested != null) {
                IconButton(
                    onClick = onBackRequested
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
            if (onLogoutRequested != null) {
                IconButton(
                    onClick = onLogoutRequested
                ){
                    Icon( Icons.Default.Logout, contentDescription = null )
                }
            }
        },
        actions = {
            if (onInfoRequested != null) {
                IconButton(
                    onClick = onInfoRequested
                ) {
                    Icon( Icons.Default.Info, contentDescription = null )
                }
            }
            if (onRefreshRequested != null) {
                IconButton(
                    onClick = onRefreshRequested
                ) {
                    Icon( Icons.Default.Refresh, contentDescription = null )
                }
            }
        }
    )
}