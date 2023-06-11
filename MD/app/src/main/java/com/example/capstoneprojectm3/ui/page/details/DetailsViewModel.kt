package com.example.capstoneprojectm3.ui.page.details

import androidx.lifecycle.ViewModel
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.ui.page.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailsViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    private val _uiState: MutableStateFlow<DetailsUiState> =
        MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState>
        get() = _uiState

    suspend fun refreshState(noteId: String){
        repository.homeNoteList.collect{ homeNoteList ->
            _uiState.value = DetailsUiState(
                isLoading = false,
                isSuccess = true,
                noteDetails = homeNoteList.find{ it.noteId == noteId} ?: Note()
            )
        }
    }
}

data class DetailsUiState (
    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val noteDetails: Note = Note()
)