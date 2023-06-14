package com.example.capstoneprojectm3.ui.page.details

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.ViewModelFactory
import com.example.capstoneprojectm3.di.Injection
import com.example.capstoneprojectm3.ui.component.DetailsTopBar
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    noteId: String,
    onNavigateToHome: () -> Unit = {},
//    onDeleteNote: () -> Unit = {},
    viewModel: DetailsViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(),
            DatastorePreferences.getInstance(LocalContext.current.dataStore))
    )
) {
    val context = LocalContext.current
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    val note = uiState.noteDetails

    var updatedTitle by rememberSaveable { mutableStateOf("") }
    if(updatedTitle == "") updatedTitle = uiState.noteDetails.title
    var updatedDescription by rememberSaveable { mutableStateOf("") }
    if(updatedDescription == "") updatedDescription = uiState.noteDetails.description

    var isUpdatingNote by rememberSaveable { mutableStateOf(false) }
    var isLoadingImage by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit){
        viewModel.refreshState(noteId)
        isUpdatingNote = isUpdatingNote(note.title, updatedTitle, note.description, updatedDescription)
    }

    Scaffold(
        topBar = { DetailsTopBar(
            note.title,
            showDelete = true,
            onNavigateToHome = { onNavigateToHome() },
            onDeleteNote = { viewModel.deleteNote(note.noteId, context, onNavigateToHome) }
        ) } ) { innerPadding ->

        Column( modifier = Modifier.padding(innerPadding) ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = "Digital Note",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = "Original Note",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            if(selectedTabIndex == 0) {
                Box {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {
                        Text(
                            text = "Last updated at: ${note.updated}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 4.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                        OutlinedTextField(
                            value = updatedTitle,
                            onValueChange = {
                                updatedTitle = it
                                isUpdatingNote = note.title != updatedTitle || note.description != updatedDescription
                            },
                            label = { Text("Note Title") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = updatedDescription,
                            onValueChange = {
                                updatedDescription = it
                                isUpdatingNote = note.title != updatedTitle || note.description != updatedDescription
                            },
                            label = { Text("Note Description") },
                            modifier = Modifier
                                .fillMaxSize(),
                        )
                    }
                    if(isUpdatingNote) {
                        val modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 80.dp)
                        if(uiState.isLoading){
                            CircularProgressIndicator(modifier = modifier)
                        } else {
                            Button(
                                onClick = { viewModel.updateNote(note.noteId, updatedTitle, updatedDescription, context) },
                                modifier = modifier
                            ) {
                                Text("Update Note")
                            }
                        }
                    }
                }
            }
            if(selectedTabIndex == 1) {
                if(isLoadingImage) {
                    CircularProgressIndicator(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp))
                }
                AsyncImage(
                    model = note.imageUrl,
                    contentDescription = "physical note image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    onState = { state ->
                        isLoadingImage = state is AsyncImagePainter.State.Loading
                    },
                )
            }
        }
    }
}

fun isUpdatingNote(sourceTitle: String, currentTitle: String, sourceDescription: String, currentDescription: String): Boolean {
    return sourceTitle != currentTitle || sourceDescription != currentDescription
}

@Preview(
    name = "Details",
    widthDp = 360,
    heightDp = 640,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun DetailsPreview() {
    CapstoneProjectM3Theme {
        Surface {
//            val noteId = "1"
//            val title = "Note Title 1"
//            val date = "DD/MM/YYYY 12:34:56"
//            val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
//            val noteExample = Note(
//                noteId,
//                title,
//                date,
//                description
//            )
//            Details(noteExample)
        }
    }
}