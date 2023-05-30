package com.example.capstoneprojectm3.ui.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capstoneprojectm3.ui.component.DetailsTopBar
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDetails: () -> Unit = {},
) {
    Scaffold(
        topBar = { DetailsTopBar("Capture Note", showDelete = false, onNavigateToHome = { onNavigateToHome() }) },
        floatingActionButton = {
            FloatingActionButton( onClick = { onNavigateToDetails() }, modifier = Modifier.padding(80.dp) ) {
                Icon(painterResource(id = com.example.capstoneprojectm3.R.drawable.outline_photo_camera_24), "Capture note")
//                Icon(Icons.Outlined.Add, "Capture note")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)){
                Text("Camera image here")
            }
        }
    )
}

@Preview(
    name = "Add Note",
    widthDp = 360,
    heightDp = 640,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AddNotePreview() {
    CapstoneProjectM3Theme {
        Surface {
            AddNote()
        }
    }
}