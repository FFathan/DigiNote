package com.example.capstoneprojectm3.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.capstoneprojectm3.R

@Composable
fun HomeTopBar(onLogout: () -> Unit = {}) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Home"
            )
        },
        navigationIcon = {
            IconButton(onClick = { onLogout() }) {
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
}
