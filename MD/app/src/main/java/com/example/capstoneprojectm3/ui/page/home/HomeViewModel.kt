package com.example.capstoneprojectm3.ui.page.home

import androidx.lifecycle.ViewModel
import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    fun fetchNoteList() {
        _uiState.value = HomeUiState(
            isLoading = false,
            isSuccess = true,
            noteList = getHomeNoteListExample())
    }
}

data class HomeUiState (
    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val noteList: List<Note> = listOf()
)

fun getHomeNoteListExample(): List<Note> {
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
    return listNoteExample
}