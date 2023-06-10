package com.example.capstoneprojectm3.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState


    fun isRepositoryAuthorized(): Boolean {
        return repository.isAuthorized
    }
    fun authorizeRepository(){
        viewModelScope.launch {
            preferences.getAuthToken().collect{ authToken ->
                repository.authorizeApiService(authToken)
            }
        }
    }

    fun fetchNoteList() {
        viewModelScope.launch {
            preferences.getAuthToken().collect{ authToken ->
                repository.mockGetAllNotes(authToken, 1, 1)
                    .collect { noteList ->
                        _uiState.value = HomeUiState(
                            isLoading = false,
                            isSuccess = true,
                            noteList = noteList)
                    }
            }
        }
    }

    fun logout(onNavigateToLogin: () -> Unit) {
        viewModelScope.launch {
            preferences.setLoginStatus(false)
            onNavigateToLogin()
        }
    }
}

data class HomeUiState (
    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val noteList: List<Note> = listOf()
)