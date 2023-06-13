package com.example.capstoneprojectm3.ui.page.addnote

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.capstoneprojectm3.BuildConfig
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.ViewModelFactory
import com.example.capstoneprojectm3.di.Injection
import com.example.capstoneprojectm3.ui.component.DetailsTopBar
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDetails: (noteId: String) -> Unit = {},
    viewModel: AddNoteViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(),
            DatastorePreferences.getInstance(LocalContext.current.dataStore))
    )
) {
    var title by rememberSaveable { mutableStateOf("") }
    var isImageCaptured by rememberSaveable { mutableStateOf(false) }
    val isAdding by viewModel.isAdding.collectAsState()


    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if(isSuccess) {
                capturedImageUri = uri
                isImageCaptured = true
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isPermissionAllowed ->
        if (isPermissionAllowed) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { DetailsTopBar("Capture Note", showDelete = false, onNavigateToHome = { onNavigateToHome() }) },
        floatingActionButton = {
            if(!isImageCaptured){
                FloatingActionButton(
                    onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.padding(80.dp) ) {
                    Icon(painterResource(id = com.example.capstoneprojectm3.R.drawable.outline_photo_camera_24), "Capture note")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it},
                    label = { Text("Add Note Title") },
                    singleLine = true
                )
                Button(
                    onClick = {
                        viewModel.addNote(uri, title, context, onNavigateToDetails)
                    },
                    enabled = title.isNotEmpty() && isImageCaptured && !isAdding
                ) {
                    Text("Create New Note")
                }
                if(isAdding) {
                    CircularProgressIndicator()
                }

                if (capturedImageUri.path?.isNotEmpty() == true) {
                    Image(
                        modifier = Modifier.padding(16.dp, 8.dp),
                        painter = rememberAsyncImagePainter(capturedImageUri),
                        contentDescription = "captured image"
                    )
                }
            }
        }
    )
}


fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
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