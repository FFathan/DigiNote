package com.example.capstoneprojectm3.ui.page.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    init{
        Log.d("HomeViewModel", "")
    }

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

    fun isHomeRequireUpdate(): Boolean{
        return repository.isHomeRequireUpdate
    }

    fun fetchNoteList() {
        viewModelScope.launch {
//            preferences.getAuthToken().collect{ authToken ->
//                repository.mockGetAllNotes(authToken, 1, 1)
//            }
            _uiState.value = HomeUiState(
                isLoading = true,
            )
            try{
                repository.getAllNotes()
                _uiState.value = HomeUiState(
                    isLoading = false,
                    isSuccess = true,
                    noteList = repository.homeNoteList
                )
            } catch (e: Exception){
                _uiState.value = HomeUiState(
                    isLoading = false,
                    isFailed = true,
                )
                setHomeRequireUpdate(false)
            }
        }
    }

    fun setHomeRequireUpdate(isRequireUpdate: Boolean) {
        repository.isHomeRequireUpdate = isRequireUpdate
    }

    fun refreshState(){
        _uiState.value = HomeUiState(
            isLoading = false,
            isSuccess = true,
            noteList = repository.homeNoteList
        )
    }

    fun logout(onNavigateToLogin: () -> Unit) {
        viewModelScope.launch {
            preferences.setLoginStatus(false)
            repository.unauthorizeApiService()
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