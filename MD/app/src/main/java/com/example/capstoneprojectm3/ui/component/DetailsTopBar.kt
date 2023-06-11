package com.example.capstoneprojectm3.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.capstoneprojectm3.R

@Composable
fun DetailsTopBar(
    noteTitle: String,
    showDelete: Boolean = true,
    onNavigateToHome: () -> Unit = {},
    onDeleteNote: () -> Unit = {}
) {
    var isDeletingNote by rememberSaveable { mutableStateOf(false) }
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
                IconButton(onClick = { onDeleteNote() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete note"
                    )
                }
            }
        }
    )
    if(isDeletingNote){
        AlertDialog(
            onDismissRequest = { isDeletingNote = false },
            icon = { Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "delete icon") },
            title = { Text(text = "Delete Note") },
            text = { Text("Do you really want to delete this note? ") },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDeletingNote = false
                        onDeleteNote()
                    }
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDeletingNote = false
                    }
                ) { Text("Cancel") }
            }
        )
    }
}
