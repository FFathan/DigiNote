package com.example.capstoneprojectm3.ui.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capstoneprojectm3.ui.component.DetailsTopBar
import com.example.capstoneprojectm3.ui.component.HomeTopBar
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(note: Note) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold( topBar = { DetailsTopBar(note.title) } ) { innerPadding ->
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
                Text(
                    text = note.description,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
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
            val noteId = 1
            val title = "Note Title 1"
            val date = "DD/MM/YYYY 12:34:56"
            val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
            val noteExample = Note(
                noteId,
                title,
                date,
                description
            )
            Details(noteExample)
        }
    }
}