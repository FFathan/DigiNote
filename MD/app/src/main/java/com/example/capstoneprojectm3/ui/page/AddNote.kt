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
    var isImageCaptured by rememberSaveable { mutableStateOf(false) }
    var isPermissionGiven by rememberSaveable { mutableStateOf(false) }

    var capturedBitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { result ->
        if (result) {
            // Handle the captured image
            isImageCaptured = true
            capturedBitmap = getLastImageBitmap(context)
        } else {
            // Handle failure or cancellation
            capturedBitmap = null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
//            takePictureLauncher.launch(createOutputUri(context))
            isPermissionGiven = true
        } else {
            // Handle permission denied
        }
    }

    LaunchedEffect(Unit){
        permissionLauncher.launch(permissionState.permission)
    }

    Scaffold(
        topBar = { DetailsTopBar("Capture Note", showDelete = false, onNavigateToHome = { onNavigateToHome() }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(isPermissionGiven) {
                        if (permissionState.hasPermission) {
                            takePictureLauncher.launch(createOutputUri(context))
                        } else {
                            permissionLauncher.launch(permissionState.permission)
                        }
                    } else {

                    }
                },
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
                    enabled = isImageCaptured
                ) {
                    Text("Create New Note")
                }
                if (capturedBitmap != null) {
                    Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }
//                Image(
//                    painter = painterResource(androidx.compose.foundation.layout.R.drawable.profile_image),
//                    contentDescription = "captured image",
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                        .height(360.dp)
//                )
            }
        }
    )
}

@Composable
fun rememberPermissionState(permission: String): PermissionState {
    val permissionState = remember { PermissionState() }

    LaunchedEffect(permission) {
        permissionState.permission = permission
    }

    return permissionState
}

class PermissionState {
    var permission: String by mutableStateOf("")
    val hasPermission: Boolean
        get() = permission.isNotEmpty()
}

@Composable
fun requestPermissionLauncher(permission: String): ActivityResultLauncher<String> {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Handle permission result
    }

    DisposableEffect(permission) {
        launcher.launch(permission)
        onDispose { }
    }

    return launcher
}

private const val AUTHORITY = "com.example.capstoneprojectm3"
private fun createOutputUri(context: Context): Uri {
    val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "IMG_${timeStamp}.jpg"
    val file = File(imagesDir, fileName)
    return FileProvider.getUriForFile(context, AUTHORITY, file)
}

@SuppressLint("Range")
private fun getLastImageBitmap(context: Context): Bitmap? {
    val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
    val sortOrder = "${MediaStore.Images.ImageColumns._ID} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        if (cursor.moveToNext()) {
            val imageUri = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
            val imageUriWithPath = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageUri.toString())

            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUriWithPath)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUriWithPath)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }
    return null
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