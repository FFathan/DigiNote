package com.example.capstoneprojectm3.ui.page.details

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.ui.page.home.HomeUiState
import com.example.capstoneprojectm3.utils.extractMessageFromJson
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

    fun updateNote(noteId: String, title: String, description: String, context: Context) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState(isLoading = true)
            val updateNoteResponse = repository.mockUpdateNote("authToken", noteId, title, "date", description)
            val isUpdated = !updateNoteResponse.error
            if(isUpdated) {
                _uiState.value = DetailsUiState(
                    isLoading = false, isSuccess = true,
                    noteDetails = repository.homeNoteList.find{ it.noteId == noteId} ?: Note())
                Toast.makeText(context, "Update Succeed", Toast.LENGTH_SHORT).show()
            } else {
                _uiState.value = DetailsUiState(
                    isLoading = false, isFailed = true,
                    noteDetails = repository.homeNoteList.find{ it.noteId == noteId} ?: Note())
                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

data class DetailsUiState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val noteDetails: Note = Note()
)