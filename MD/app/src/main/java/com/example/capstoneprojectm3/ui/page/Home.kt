package com.example.capstoneprojectm3.ui.page

import android.content.res.Configuration
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capstoneprojectm3.ui.component.HomeTopBar
import com.example.capstoneprojectm3.ui.component.NoteCard
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    noteList: List<Note>,
    onNavigateToDetails: () -> Unit = {},
    onNavigateToAddNote: () -> Unit = {},
) {
    Scaffold(
        topBar = { HomeTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToAddNote() },
                modifier = Modifier.padding(end = 40.dp, bottom = 40.dp)) {
                Icon(Icons.Filled.Add, "Add new note")
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
                items(noteList){note ->
                    NoteCard(
                        note.title,
                        note.date,
                        note.description,
                        modifier = Modifier.clickable(onClick = { onNavigateToDetails() })
                    )
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
        Surface(
//            modifier = Modifier
//                .width(360.dp)
//                .height(640.dp),
//            color = MaterialTheme.colorScheme.background
        ) {
            val title = "Note Title"
            val date = "DD/MM/YYYY 12:34:56"
            val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
            val listNoteExample = mutableListOf<Note>()
            for (id in 1..100) {
                val note = Note(
                    id,
                    "$title $id",
                    date,
                    description
                )
                listNoteExample.add(note)
            }
            Home(listNoteExample)
        }
    }
}