package com.example.capstoneprojectm3.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.capstoneprojectm3.R

@Composable
fun HomeTopBar(onLogout: () -> Unit = {}) {
    var isLoggingOut by rememberSaveable { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Home"
            )
        },
        navigationIcon = {
            IconButton(onClick = { isLoggingOut = true }) {
                Icon(
                    painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = "logout")
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search something"
                )
            }
        }
    )
    if(isLoggingOut){
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                isLoggingOut = false
            },
            icon = { Icon(
                painterResource(R.drawable.baseline_logout_24) ,
                contentDescription = "logout icon") },
            title = {
                Text(text = "Logout")
            },
            text = {
                Text("Do you really want to log out from this account? ")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isLoggingOut = false
                        onLogout()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isLoggingOut = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
