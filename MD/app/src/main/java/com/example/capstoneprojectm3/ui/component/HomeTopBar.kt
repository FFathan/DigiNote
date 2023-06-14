package com.example.capstoneprojectm3.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import com.example.capstoneprojectm3.R

@Composable
fun HomeTopBar(
    onLogout: () -> Unit = {},
    searchQuery: String,
    onSearchQueryChanged: (searchQuery: String) -> Unit = {},
    setIsSearching: (isSearching: Boolean) -> Unit = {},
    isSearching: Boolean = false
) {
    var isLoggingOut by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            if(isSearching) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { onSearchQueryChanged(it) },
                    placeholder = { Text("Search your note") },
                )
            } else {
                Text(
                    "Home"
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { isLoggingOut = true }) {
                Icon(
                    painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = "logout")
            }
        },
        actions = {
            if(isSearching) {
                IconButton(onClick = { setIsSearching(false) }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close search"
                    )
                }
            } else {
                IconButton(onClick = { setIsSearching(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search something"
                    )
                }
            }

        },
    )
    if(isLoggingOut){
        AlertDialog(
            onDismissRequest = { isLoggingOut = false },
            icon = { Icon(
                painterResource(R.drawable.baseline_logout_24) ,
                contentDescription = "logout icon") },
            title = { Text(text = "Logout") },
            text = { Text("Do you really want to log out from this account? ") },
            confirmButton = {
                TextButton(
                    onClick = {
                        isLoggingOut = false
                        onLogout()
                    }
                ) { Text("Logout") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isLoggingOut = false
                    }
                ) { Text("Cancel") }
            }
        )
    }
}
