package com.example.capstoneprojectm3.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailsTopBar(
    noteTitle: String,
    showDelete: Boolean = true,
    onNavigateToHome: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                noteTitle
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateToHome() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to home"
                )
            }
        },
        actions = {
            if(showDelete) {
                IconButton(onClick = { onNavigateToHome() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete note"
                    )
                }
            }
        }
    )
}
