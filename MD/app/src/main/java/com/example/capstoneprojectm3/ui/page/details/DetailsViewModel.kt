package com.example.capstoneprojectm3.ui.page.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.ui.page.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    private val _uiState: MutableStateFlow<DetailsUiState> =
        MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState>
        get() = _uiState

    fun refreshState(noteId: String){
        _uiState.value = DetailsUiState(
            isLoading = false,
            isSuccess = true,
            noteDetails = repository.homeNoteList.find{ it.noteId == noteId} ?: Note()
        )
    }

    fun deleteNote(noteId: String, onNavigateToHome: () -> Unit) {
        viewModelScope.launch {
            preferences.getAuthToken().collect { authToken ->
                repository.mockDeleteNote(authToken, noteId)
                onNavigateToHome()
            }
        }
    }
}

data class DetailsUiState (
    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val noteDetails: Note = Note()
)