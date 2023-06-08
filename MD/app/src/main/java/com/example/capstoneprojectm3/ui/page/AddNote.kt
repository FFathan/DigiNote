package com.example.capstoneprojectm3.ui.page

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.capstoneprojectm3.ui.component.DetailsTopBar
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDetails: () -> Unit = {},
) {
    var title by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { DetailsTopBar("Capture Note", showDelete = false, onNavigateToHome = { onNavigateToHome() }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                modifier = Modifier.padding(80.dp) ) {
                Icon(painterResource(id = com.example.capstoneprojectm3.R.drawable.outline_photo_camera_24), "Capture note")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it},
                    label = { Text("Add Note Title") }
                )
                Button(
                    onClick = {},
                ) {
                    Text("Create New Note")
                }

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