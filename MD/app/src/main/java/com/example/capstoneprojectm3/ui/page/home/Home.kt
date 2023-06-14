package com.example.capstoneprojectm3.ui.page.home

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.capstoneprojectm3.ui.component.HomeTopBar
import com.example.capstoneprojectm3.ui.component.NoteCard
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.ViewModelFactory
import com.example.capstoneprojectm3.di.Injection

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onNavigateToDetails: (noteId: String) -> Unit = {},
    onNavigateToAddNote: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(),
        DatastorePreferences.getInstance(LocalContext.current.dataStore))
    )
) {
    Log.d("home", "")
    LaunchedEffect(Unit){
        if(!viewModel.isRepositoryAuthorized()) viewModel.authorizeRepository()
        viewModel.refreshState()
    }
    if(viewModel.isHomeRequireUpdate() && viewModel.isRepositoryAuthorized()) viewModel.fetchNoteList()

    val uiState by viewModel.uiState.collectAsState()
    val noteList = uiState.noteList

    Scaffold(
        topBar = { HomeTopBar(onLogout = { viewModel.logout(onNavigateToLogin) }) },
        floatingActionButton = {
            if(uiState.isSuccess){
                FloatingActionButton(
                    onClick = { onNavigateToAddNote() },
                    modifier = Modifier.padding(end = 40.dp, bottom = 40.dp)) {
                    Icon(Icons.Filled.Add, "Add new note")
                }
            }
        },

        content = { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = innerPadding,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                if(uiState.isLoading) {
                    items(10) {
                        NoteCard(
                            "Loading...",
                            "Loading...",
                            "Loading...",
                            modifier = Modifier.clickable(onClick = {})
                        )
                    }
                }
                if(uiState.isSuccess) {
                    if(noteList.isEmpty()) {
                        item {
                            Text("No notes found")
                        }
                    }
                    items(noteList){note ->
                        NoteCard(
                            note.title,
                            note.date,
                            note.description,
                            modifier = Modifier.clickable(onClick = { onNavigateToDetails(note.noteId) })
                        )
                    }
                }
                if(uiState.isFailed) {
                    items(1) {
                        Button(onClick = {
                            viewModel.setHomeRequireUpdate(true)
                            viewModel.fetchNoteList()
                        }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    )
}

@Preview(
    name = "Home",
    widthDp = 360,
    heightDp = 640,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HomePreview() {
    CapstoneProjectM3Theme {
        Surface {
//            Home(listNoteExample)
        }
    }
}