package com.example.capstoneprojectm3.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    fun isRepositoryAuthorized(): Boolean {
        return repository.isAuthorized
    }
    fun authorizeRepository(authToken: String){
        repository.authorizeApiService(authToken)
    }

    fun fetchNoteList() {
        viewModelScope.launch {
//            repository.getDummyListNote()
//                .collect { noteList ->
//                    _uiState.value = HomeUiState(
//                        isLoading = false,
//                        isSuccess = true,
//                        noteList = noteList)
//                }
            repository.mockGetAllNotes("auth-token", 1, 1)
                .collect { noteList ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        isSuccess = true,
                        noteList = noteList)
                }
        }
    }
}

data class HomeUiState (
    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val noteList: List<Note> = listOf()
)